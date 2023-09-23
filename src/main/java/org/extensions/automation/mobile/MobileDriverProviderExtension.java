package org.extensions.automation.mobile;

import lombok.extern.slf4j.Slf4j;
import org.base.configuration.PropertiesManager;
import org.base.mobile.*;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.factory.JunitReflectionAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class MobileDriverProviderExtension implements
        ParameterResolver, BeforeEachCallback,
        AfterEachCallback, AfterAllCallback,
        JunitReflectionAnnotationHandler.ExtensionContextHandler {

    private final ThreadLocal<MobileDriverProvider> driverManager = new ThreadLocal<>();
    private final AtomicReference<AppiumServiceManager> appiumServiceManager = new AtomicReference<>();
    private final ThreadLocal<MobileConfiguration> mobileProperties = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        return parameterContext.getParameter().getType() == MobileDriverProvider.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<DriverProvider> provider = this.readAnnotation(context, DriverProvider.class);
            if (provider.isPresent()) return this.driverManager.get();
            throw new RuntimeException("MobileDriverProviderExtension error DriverJsonProvider");
        }
        throw new RuntimeException("MobileDriverProviderExtension error DriverJsonProvider");
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {

                Optional<DriverProvider> provider = this.readAnnotation(context, DriverProvider.class);
                if (provider.isPresent()) {

                    this.mobileProperties.set(new PropertiesManager().getOrCreate(MobileConfiguration.class));
                    CapsReaderAdapter capsReader = new CapsReaderAdapter(provider.get().jsonCapabilitiesFile());
                    String client = capsReader.getJsonObject().getClient();
                    DesiredCapabilities extra = this.setCapabilitiesExtra(client, provider.get());
                    capsReader.getCapabilities().merge(extra);

                    this.appiumServiceManager.set(new AppiumServiceManager(
                            capsReader.getJsonObject().getNodeJs(),
                            capsReader.getJsonObject().getAppiumExe(),
                            capsReader.getJsonObject().getAppiumIp(),
                            Integer.parseInt(capsReader.getJsonObject().getAppiumPort()),
                            capsReader.getJsonObject().getAndroidHome()
                    ));
                    
                    DriverType driverType = this.getDriverType(client);
                    this.driverManager.set(new MobileDriverProvider(driverType, capsReader.getCapabilities(), capsReader.getJsonObject().getDriverUrl(), provider.get().implicitlyWait()));

                    ApplicationLaunchOption applicationLaunchOption = provider.get().appLaunchOption();
                    this.driverManager.get().getAppLauncherExtensions().applicationLaunchOptions(applicationLaunchOption, capsReader.getJsonObject().getAppBundleId());

                } else throw new RuntimeException("MobileDriverProviderExtension error DriverProvider is not visible");

            } else throw new RuntimeException("MobileDriverProviderExtension error DriverProvider is not visible");
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {

            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
            }
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext extensionContext)  {
        if (extensionContext.getElement().isPresent()) {
            try {
                if (this.appiumServiceManager.get() != null) {
                    this.appiumServiceManager.get().close();
                }
            } catch (Exception ignore) {}
        }
    }


    private synchronized DesiredCapabilities setCapabilitiesExtra(String clientType, DriverProvider jsonProvider) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        switch (clientType) {
            case "ANDROID" -> {
                if (jsonProvider.androidExtraCapsKeys() != null && jsonProvider.androidExtraCapsKeys().length > 0
                        && jsonProvider.androidExtraValuesValues() != null && jsonProvider.androidExtraValuesValues().length > 0
                        && jsonProvider.androidExtraCapsKeys().length == jsonProvider.androidExtraValuesValues().length) {
                    DesiredCapabilities extra = this.setDesiredCapabilities(jsonProvider.androidExtraCapsKeys(), jsonProvider.androidExtraValuesValues());
                    desiredCapabilities.merge(extra);
                }
            }
            case "IOS" -> {
                if (jsonProvider.iosExtraCapsKeys() != null && jsonProvider.iosExtraCapsKeys().length > 0
                        && jsonProvider.iosExtraCapsValues() != null && jsonProvider.iosExtraCapsValues().length > 0
                        && jsonProvider.iosExtraCapsKeys().length == jsonProvider.iosExtraCapsValues().length) {
                    DesiredCapabilities extra = this.setDesiredCapabilities(jsonProvider.iosExtraCapsKeys(), jsonProvider.iosExtraCapsValues());
                    desiredCapabilities.merge(extra);
                }
            }
            default -> throw new RuntimeException("no android or ios driver name was provided");
        }

        return desiredCapabilities;
    }

    private synchronized DesiredCapabilities setDesiredCapabilities(String[] keys, String[] values) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        for (int i = 0; i < keys.length; i++) {
            capabilities.setCapability(keys[i], values[i]);
        }

        return capabilities;
    }

    private synchronized DriverType getDriverType(String client) {
        return switch (client) {
            case "ANDROID" -> DriverType.ANDROID;
            case "IOS" -> DriverType.IOS;
            default -> DriverType.UNKNOWN;
        };
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

    // private final ThreadLocal<MobProxyExtension> mobProxyExtension = new ThreadLocal<>();

    //private final ThreadLocal<List<LogEntry>> logEntries = new ThreadLocal<>();
    // this.mobProxyExtension.set(new MobProxyExtension(ProxyType.MOBILE, Inet4Address.getLocalHost()));

    //this.logEntries.set(this.driverManager.get().getMobileDriver().manage().logs().get("logcat").getAll());
//                if (this.mobProxyExtension.get().getServer() != null && this.mobProxyExtension.get().getServer().getHar() != null) {
//                    String testName = context.getRequiredTestMethod().getName();
//                    String dir = System.getProperty("user.dir") + "/" + "target/harFiles";
//                    String testPath = dir + "/" + testName + ".json";
//                    FilesHelper filesHelper = new FilesHelper();
//                    filesHelper.createDirectory(dir);
//                    this.mobProxyExtension.get().writeHarFile(new File(testPath), this.mobProxyExtension.get().getServer().getHar().getLog());
//                    List<LogEntryObject> logEntryObjects = new ArrayList<>();
//                    logEntryObjects.add(new LogEntryObject(this.logEntries.get()));
//                    String path = System.getProperty("user.dir") + "/" + this.mobileProperties.get().entryFileLocation();
//                    JacksonObjectAdapter<LogEntryObject> jacksonHelper = new JacksonObjectAdapter<>(path, new File(path + "/" + this.mobileProperties.get().entryFileLocation() + "/" + testName + ".json"), LogEntryObject.class);
//                    jacksonHelper.writeToJson(true, logEntryObjects);
//                }

    //        if (this.mobProxyExtension.get() != null
//                && this.mobProxyExtension.get().getProxy() != null
//                && this.mobProxyExtension.get().getServer().isStarted()) {
//            this.mobProxyExtension.get().getServer().stop();
//        }

}
