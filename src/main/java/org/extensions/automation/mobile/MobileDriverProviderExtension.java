package org.extensions.automation.mobile;

import lombok.extern.slf4j.Slf4j;
import org.base.configuration.PropertiesManager;
import org.base.mobile.MobileConfiguration;
import org.base.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.mobile.appium.AndroidServerArgumentsInjections;
import org.extensions.anontations.mobile.appium.AppiumServerArgumentsInjections;
import org.extensions.anontations.mobile.appium.IosServerArgumentsInjections;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.automation.proxy.ProxyType;
import org.extensions.factory.JunitAnnotationHandler;
import org.data.files.jsonReader.FilesHelper;
import org.data.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.base.mobile.MobileDriverProvider.isAndroidClient;

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
        return clazz == MobileDriverProvider.class && parameterContext.isAnnotated(DriverProvider.class);
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<DriverProvider> provider = this.readAnnotation(context, DriverProvider.class);
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
                Optional<DriverProvider> provider = this.readAnnotation(context, DriverProvider.class);
                if (provider.isPresent()) {
                    this.mobProxyExtension.set(new MobProxyExtension(ProxyType.MOBILE, Inet4Address.getLocalHost()));
                    this.mobileProperties.set(new PropertiesManager().getOrCreate(MobileConfiguration.class));
                    this.mobileProperties.get().setProperty("android.caps.json", provider.get().jsonCapsPath());
                    Optional<AppiumServerArgumentsInjections> serverArguments = this.readAnnotation(context, AppiumServerArgumentsInjections.class);
                    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                    serverArguments.ifPresent(appiumServerArgumentsInjections -> desiredCapabilities.merge(this.setCapabilitiesExtra(appiumServerArgumentsInjections)));
                    CapsReaderAdapter capsReaderAdapter = new CapsReaderAdapter(this.mobileProperties.get().mobileJsonCapabilities());
                    desiredCapabilities.merge(capsReaderAdapter.getCapabilities());
                    this.driverManager.set(new MobileDriverProvider(capsReaderAdapter));
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

    private DesiredCapabilities setCapabilitiesExtra(AppiumServerArgumentsInjections serverArguments) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        if (serverArguments != null) {
            if (isAndroidClient()) {
                AndroidServerArgumentsInjections androidServerArguments = serverArguments.android();
                if (androidServerArguments.keys().length > 0) {
                    for (int i = 0; i < androidServerArguments.keys().length; i++) {
                        desiredCapabilities.setCapability(androidServerArguments.keys()[i], androidServerArguments.values()[i]);
                    }
                }
            } else {
                IosServerArgumentsInjections iosServerArguments = serverArguments.ios();
                if (iosServerArguments.keys().length > 0) {
                    for (int i = 0; i < iosServerArguments.keys().length; i++) {
                        desiredCapabilities.setCapability(iosServerArguments.keys()[i], iosServerArguments.values()[i]);
                    }
                }
            }
        }

        return desiredCapabilities;
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
