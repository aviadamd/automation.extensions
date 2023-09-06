package org.extensions.automation.mobile;

import lombok.extern.slf4j.Slf4j;
import org.base.configuration.PropertiesManager;
import org.base.mobile.AppiumServiceManager;
import org.base.mobile.MobileConfiguration;
import org.base.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.automation.proxy.ProxyType;
import org.extensions.factory.JunitReflectionAnnotationHandler;
import org.data.files.jsonReader.FilesHelper;
import org.data.files.jsonReader.JacksonObjectAdapter;
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

@Slf4j
public class MobileDriverProviderExtension implements
        ParameterResolver,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        JunitReflectionAnnotationHandler.ExtensionContextHandler {
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
            } else throw new RuntimeException("MobileDriverProviderExtension error DriverJsonProvider");
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

                    CapsReaderAdapter capsReaderAdapter = new CapsReaderAdapter(this.mobileProperties.get().mobileJsonCapabilitiesLocation());
                    DesiredCapabilities desiredCapabilities = new DesiredCapabilities(capsReaderAdapter.getCapabilities());
                    DesiredCapabilities setCapabilitiesExtra = this.setCapabilitiesExtra(capsReaderAdapter, provider.get());
                    desiredCapabilities.merge(setCapabilitiesExtra);

                    AppiumServiceManager appiumServiceManager = new AppiumServiceManager(
                            "C:/Program Files/nodejs/node.exe",
                            "C:/Users/Lenovo/AppData/Roaming/npm/node_modules/appium/build/lib/main.js",
                            "0.0.0.0",
                            4723);
                    appiumServiceManager.initService();

                    this.driverManager.set(new MobileDriverProvider(capsReaderAdapter.getJsonObject().getClient(), capsReaderAdapter.getCapabilities(), capsReaderAdapter.getJsonObject().getDriverUrl()));
                    this.logEntries.set(this.driverManager.get().getMobileDriver().manage().logs().get("logcat").getAll());
                }
            } else throw new RuntimeException("MobileDriverProviderExtension error DriverProvider is not visible");
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
                    JacksonObjectAdapter<LogEntryObject> jacksonHelper = new JacksonObjectAdapter<>(path, new File(path + "/" + this.mobileProperties.get().entryFileLocation() + "/" + testName + ".json"), LogEntryObject.class);
                    jacksonHelper.writeToJson(true, logEntryObjects);
                }
            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext)  {
        if (this.mobProxyExtension.get() != null
                && this.mobProxyExtension.get().getProxy() != null
                && this.mobProxyExtension.get().getServer().isStarted()) {
            this.mobProxyExtension.get().getServer().stop();
        }
    }

    private synchronized DesiredCapabilities setCapabilitiesExtra(CapsReaderAdapter capsReaderAdapter, DriverProvider driverJsonProvider) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        String clientType = capsReaderAdapter.getJsonObject().getClient();
        switch (clientType) {
            case "ANDROID" -> {
                if (driverJsonProvider.androidExtraCapsKeys() != null && driverJsonProvider.androidExtraCapsKeys().length > 0
                        && driverJsonProvider.androidExtraValuesValues() != null && driverJsonProvider.androidExtraValuesValues().length > 0
                        && driverJsonProvider.androidExtraCapsKeys().length == driverJsonProvider.androidExtraValuesValues().length) {
                    DesiredCapabilities extra = capsReaderAdapter.setDesiredCapabilities(driverJsonProvider.androidExtraCapsKeys(), driverJsonProvider.androidExtraValuesValues());
                    desiredCapabilities.merge(extra);
                }
            }
            case "IOS" -> {
                if (driverJsonProvider.iosExtraCapsKeys() != null && driverJsonProvider.iosExtraCapsKeys().length > 0
                        && driverJsonProvider.iosExtraCapsValues() != null && driverJsonProvider.iosExtraCapsValues().length > 0
                        && driverJsonProvider.iosExtraCapsKeys().length == driverJsonProvider.iosExtraCapsValues().length) {
                    DesiredCapabilities extra = capsReaderAdapter.setDesiredCapabilities(driverJsonProvider.iosExtraCapsKeys(), driverJsonProvider.iosExtraCapsValues());
                    desiredCapabilities.merge(extra);
                }
            }
            default -> throw new RuntimeException("no android or ios driver name was provided");
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
