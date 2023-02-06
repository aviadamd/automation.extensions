package org.extensions.automation.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.proxy.CaptureType;
import org.automation.mobile.MobileDriverManager;
import org.extensions.anontations.mobile.DriverJsonProvider;
import org.extensions.factory.JunitAnnotationHandler;
import org.files.jsonReader.JacksonExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.logging.LogEntry;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import static org.automation.AutomationProperties.getPropertiesInstance;

@Slf4j
public class MobileDriverProviderExtension implements
        ParameterResolver,
        AfterEachCallback,
        AfterAllCallback,
        JunitAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<BrowserMobProxy> mobProxy = new ThreadLocal<>();
    private final ThreadLocal<MobileDriverManager> driverManager = new ThreadLocal<>();
    private final ThreadLocal<List<LogEntry>> logEntries = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        return parameterContext.getParameter().getType() == MobileDriverManager.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        if (context.getElement().isPresent() && parameterContext.getParameter().getType() == MobileDriverManager.class) {
            Optional<DriverJsonProvider> provider = this.readAnnotation(context, DriverJsonProvider.class);
            if (provider.isPresent()) {
                this.mobProxy.set(this.setMobProxyServer(context.getRequiredTestMethod().getName() + ".har", provider.get().proxyPort()));
                this.driverManager.set(new MobileDriverManager(new CapsReaderAdapter(getPropertiesInstance().getProperty(provider.get().jsonCapsPath()))));
                this.logEntries.set(this.driverManager.get().getMobileDriver().manage().logs().get("logcat").getAll());
                return this.driverManager.get();
            }
        }
        throw new RuntimeException("MobileDriverProviderExtension error DriverJsonProvider");
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                if (this.mobProxy.get() != null && this.mobProxy.get().isStarted() && this.mobProxy.get().getHar() != null) {
                    String testPath = System.getProperty("user.dir") + "/target/harFiles";
                    Path testPathDir = Paths.get(testPath);
                    if (!Files.exists(testPathDir)) Files.createDirectory(testPathDir);
                    String testName = context.getRequiredTestMethod().getName();
                    this.writeHarFile(new File(testPath + "/" + testName + ".json"), this.mobProxy.get().getHar().getLog());
                    String entriesPath = System.getProperty("user.dir") + "/target/logEntries";
                    Path entriesPathDir = Paths.get(entriesPath);
                    if (!Files.exists(entriesPathDir)) Files.createDirectory(entriesPathDir);
                    new JacksonExtensions(entriesPath + "/"+testName+"" + ".json").readAndWrite(this.logEntries.get(), LogEntry.class, true);
                }
            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext)  {
        if (this.mobProxy.get() != null && this.mobProxy.get().isStarted()) {
            this.mobProxy.get().stop();
        }
    }
    public synchronized BrowserMobProxy setMobProxyServer(String fileName, int port) {
        try {

            BrowserMobProxyServer mobProxyServer = new BrowserMobProxyServer();
            mobProxyServer.setTrustAllServers(true);
            EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();
            captureTypes.addAll(CaptureType.getCookieCaptureTypes());
            captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
            captureTypes.addAll(CaptureType.getRequestCaptureTypes());
            captureTypes.addAll(CaptureType.getResponseCaptureTypes());
            mobProxyServer.enableHarCaptureTypes(captureTypes);
            mobProxyServer.newHar(fileName);
            mobProxyServer.setConnectTimeout(5, TimeUnit.SECONDS);
            mobProxyServer.start(port);

            return mobProxyServer;
        } catch (Exception exception) {
            throw new RuntimeException("init mob proxy fails ", exception);
        }
    }
    @Override
    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                Assertions.fail("Fail read annotation from ExtentReportExtension", exception);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private synchronized void writeHarFile(File harFile, HarLog log) throws IOException {
        String version = log.getVersion();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(harFile, JsonEncoding.UTF8);

        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonGenerator.setCodec(objectMapper);
        jsonGenerator.useDefaultPrettyPrinter();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("log");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("version");
        jsonGenerator.writeObject(version);
        jsonGenerator.writeFieldName("creator");
        jsonGenerator.writeObject(log.getCreator());
        jsonGenerator.writeFieldName("pages");
        jsonGenerator.writeStartArray();

        for (HarPage page : log.getPages()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("startedDateTime");
            jsonGenerator.writeObject(dateFormat.format(page.getStartedDateTime()));
            jsonGenerator.writeFieldName("id");
            jsonGenerator.writeObject(page.getId());
            jsonGenerator.writeFieldName("title");
            jsonGenerator.writeObject(page.getTitle());
            jsonGenerator.writeFieldName("pageTimings");
            jsonGenerator.writeObject(page.getPageTimings());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeFieldName("entries");
        jsonGenerator.writeStartArray();

        for (HarEntry entry : log.getEntries()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("startedDateTime");
            jsonGenerator.writeObject(dateFormat.format(entry.getStartedDateTime()));
            jsonGenerator.writeFieldName("time");
            jsonGenerator.writeObject(entry.getTime());
            jsonGenerator.writeFieldName("request");
            jsonGenerator.writeObject(entry.getRequest());
            jsonGenerator.writeFieldName("response");
            jsonGenerator.writeObject(entry.getResponse());
            jsonGenerator.writeFieldName("timings");
            jsonGenerator.writeObject(entry.getTimings());
            jsonGenerator.writeFieldName("serverIPAddress");
            jsonGenerator.writeObject(entry.getServerIPAddress());
            jsonGenerator.writeFieldName("connection");
            jsonGenerator.writeObject(entry.getConnection());
            jsonGenerator.writeFieldName("pageref");
            jsonGenerator.writeObject(entry.getPageref());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
    }

    //  TrustSource trustSource = TrustSource.defaultTrustSource();
    //            trustSource.add(TrustUtil.getBuiltinTrustedCAs());
    //            trustSource.add(TrustUtil.getJavaTrustedCAs());
    //            trustSource.add(TrustUtil.getDefaultJavaTrustManager().getAcceptedIssuers());
    //            trustSource.add(TrustUtil.readX509CertificatesFromPem("C:\\Users\\Lenovo\\OneDrive\\Desktop\\ca-certificate-rsa.cer"));
    //            mobProxyServer.setTrustSource(trustSource);
    //
    //            mobProxyServer.setMitmDisabled(false);
    //            mobProxyServer.setMitmManager(ImpersonatingMitmManager.builder()
    //                    .trustSource(trustSource)
    //                    .trustAllServers(true)
    //                    .build());
}
