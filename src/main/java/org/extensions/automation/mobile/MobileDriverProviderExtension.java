package org.extensions.automation.mobile;

import lombok.extern.slf4j.Slf4j;
import org.automation.configuration.PropertiesManager;
import org.automation.mobile.MobileConfiguration;
import org.automation.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverJsonProvider;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.factory.JunitAnnotationHandler;
import org.files.jsonReader.FilesHelper;
import org.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.logging.LogEntry;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MobileDriverProviderExtension implements
        ParameterResolver,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        JunitAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<List<LogEntry>> logEntries = new ThreadLocal<>();
    private final ThreadLocal<MobileDriverProvider> driverManager = new ThreadLocal<>();
    private final ThreadLocal<MobProxyExtension> mobProxyExtension = new ThreadLocal<>();
    private final ThreadLocal<MobileConfiguration> mobileProperties = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        Class<?> clazz = parameterContext.getParameter().getType();
        return clazz == MobileDriverProvider.class && parameterContext.isAnnotated(DriverJsonProvider.class);
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<DriverJsonProvider> provider = this.readAnnotation(context, DriverJsonProvider.class);
            if (provider.isPresent()) {
                return this.driverManager.get();
            }
        }
        throw new RuntimeException("MobileDriverProviderExtension error DriverJsonProvider");
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<DriverJsonProvider> provider = this.readAnnotation(context, DriverJsonProvider.class);
                if (provider.isPresent()) {
                    this.mobProxyExtension.set(new MobProxyExtension(MobProxyExtension.ProxyType.MOBILE, 0, Inet4Address.getLocalHost()));
                    this.mobileProperties.set(new PropertiesManager().getOrCreate(MobileConfiguration.class));
                    this.mobileProperties.get().setProperty("android.caps.json", provider.get().jsonCapsPath());
                    this.driverManager.set(new MobileDriverProvider(new CapsReaderAdapter(this.mobileProperties.get().mobileJsonCapabilities())));
                    this.logEntries.set(this.driverManager.get().getMobileDriver().manage().logs().get("logcat").getAll());
                }
            }
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }
    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                if (this.mobProxyExtension.get().getServer() != null && this.mobProxyExtension.get().getServer().getHar() != null) {
                    String testName = context.getRequiredTestMethod().getName();
                    String dir = System.getProperty("user.dir") + "/" + "target/harFiles";
                    String testPath = dir + "/" + testName + ".json";
                    FilesHelper filesHelper = new FilesHelper();
                    filesHelper.createDirectory(dir);
                    this.mobProxyExtension.get().writeHarFile(new File(testPath), this.mobProxyExtension.get().getServer().getHar().getLog());
                    List<LogEntryObject> logEntryObjects = new ArrayList<>();
                    logEntryObjects.add(new LogEntryObject(this.logEntries.get()));
                    String path = System.getProperty("user.dir") + "/" + this.mobileProperties.get().entryFileLocation();
                    JacksonExtension<LogEntryObject> jacksonHelper = new JacksonExtension<>(path, new File(path + "/" + this.mobileProperties.get().entryFileLocation() + "/" + testName + ".json"), LogEntryObject.class);
                    jacksonHelper.writeToJson(logEntryObjects);
                }
            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext)  {
        if (this.mobProxyExtension.get().getProxy() != null && this.mobProxyExtension.get().getServer().isStarted()) {
            this.mobProxyExtension.get().getServer().stop();
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
}
