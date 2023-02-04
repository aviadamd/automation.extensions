package org.extensions.automation.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.proxy.CaptureType;
import org.automation.AutomationProperties;
import org.automation.web.SeleniumWebDriverManager;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import static org.automation.AutomationProperties.getPropertiesInstance;

@Slf4j
public class WebDriverProviderExtension implements ParameterResolver, AfterEachCallback, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<Proxy> seleniumProxy = new ThreadLocal<>();
    private final ThreadLocal<BrowserMobProxy> mobProxy = new ThreadLocal<>();
    private final ThreadLocal<DesiredCapabilities> capabilities = new ThreadLocal<>();
    private final ThreadLocal<SeleniumWebDriverManager> driverManager = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == SeleniumWebDriverManager.class;
    }
    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
            if (driverType.isPresent()) {

                this.mobProxy.set(this.setMobProxyServer(driverType.get().proxyPort(), context.getRequiredTestMethod().getName() + ".har"));
                this.seleniumProxy.set(this.setSeleniumProxy(this.mobProxy.get()));
                this.capabilities.set(new DesiredCapabilities());
                this.capabilities.get().setCapability(CapabilityType.PROXY, this.seleniumProxy.get());
                this.capabilities.get().acceptInsecureCerts();

                if (getPropertiesInstance().getProperty("project.client").equalsIgnoreCase("chrome")) {
                    this.driverManager.set(new SeleniumWebDriverManager(AutomationProperties.getPropertiesInstance().getProperty(driverType.get().baseUrl()), Duration.ofSeconds(driverType.get().generalTo()), WebDriverManager.chromedriver().capabilities(this.chromeOptions().merge(capabilities.get())).create()));
                    return this.driverManager.get();
                } else if (getPropertiesInstance().getProperty("project.client").equalsIgnoreCase("firefox")) {
                    this.driverManager.set(new SeleniumWebDriverManager(AutomationProperties.getPropertiesInstance().getProperty(driverType.get().baseUrl()), Duration.ofSeconds(driverType.get().generalTo()), WebDriverManager.firefoxdriver().capabilities(this.firefoxOptions().merge(capabilities.get())).create()));
                    return this.driverManager.get();
                }
            }
        }
        throw new RuntimeException("WebDriverProviderExtension error");
    }
    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    if (this.mobProxy.get() != null && this.mobProxy.get().getHar() != null) {
                        String testPath = System.getProperty("user.dir") + "/target/harFiles";
                        Path path = Paths.get(testPath);
                        if (!Files.exists(path)) Files.createDirectory(path);
                        String testName = context.getRequiredTestMethod().getName();
                        this.writeHarFile(new File(testPath + "/" + testName + ".json"), this.mobProxy.get().getHar().getLog());
                    }
                }
            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
            }
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent() && this.mobProxy.get() != null) {
                    this.mobProxy.get().stop();
                }
            } catch (Exception exception) {
                Assertions.fail("close proxy error ", exception);
            }
        }
    }

    @Override
    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                Assertions.fail("read annotation error ", exception);
            }
        }
        return Optional.empty();
    }

    public synchronized Proxy setSeleniumProxy(BrowserMobProxy browserMobProxy) {
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy);
            seleniumProxy.setHttpProxy(hostIp + ":" + browserMobProxy.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + browserMobProxy.getPort());
            return seleniumProxy;
        } catch (Exception exception) {
            throw new RuntimeException("init selenium proxy fails ", exception);
        }
    }
    public synchronized BrowserMobProxy setMobProxyServer(int port, String fileName) {
        try {
            BrowserMobProxyServer mobProxyServer = new BrowserMobProxyServer();
            mobProxyServer.setTrustAllServers(true);
            mobProxyServer.setMitmDisabled(true);
            EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();
            captureTypes.addAll(CaptureType.getCookieCaptureTypes());
            captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
            captureTypes.addAll(CaptureType.getRequestCaptureTypes());
            captureTypes.addAll(CaptureType.getResponseCaptureTypes());
            mobProxyServer.enableHarCaptureTypes(captureTypes);
            mobProxyServer.newHar(fileName);
            mobProxyServer.start(port);
            return mobProxyServer;
        } catch (Exception exception) {
            throw new RuntimeException("init mob proxy fails ", exception);
        }
    }

    private synchronized ChromeOptions chromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--dns-prefetch-disable");
        chromeOptions.addArguments("enable-automation");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-running-insecure-content");
        return chromeOptions;
    }

    private FirefoxOptions firefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");
        firefoxOptions.addPreference("network.automatic-ntlm-auth.allow-non-fqdn", true);
        firefoxOptions.addPreference("network.negotiate-auth.delegation-uris", "http://,https://");
        firefoxOptions.addPreference("network.negotiate-auth.trusted-uris", "http://,https://");
        firefoxOptions.addPreference("network.http.phishy-userpass-length", 255);
        firefoxOptions.addPreference("security.csp.enable", false);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setAcceptUntrustedCertificates(true);
        return firefoxOptions;
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
}

