package org.extensions.automation.mobile;

import org.base.configuration.PropertiesManager;
import org.base.mobile.*;
import org.extensions.anontations.mobile.DriverProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.utils.assertions.AssertJHandler;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class MobileDriverProviderExtension implements ParameterResolver, BeforeEachCallback, AfterAllCallback {

    private final ThreadLocal<MobileProvider> mobileProvider = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        boolean isSupportsParameter = parameterContext.getParameter().getType() == MobileProvider.class;
        if (isSupportsParameter) {
            return true;
        } else throw new RuntimeException("MobileDriverProviderExtension error MobileProvider is not visible in test parameter");
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<DriverProvider> provider = this.readAnnotation(context, DriverProvider.class);
            if (provider.isPresent()) return this.mobileProvider.get();
            else throw new RuntimeException("MobileDriverProviderExtension error MobileProvider is not visible");
        } else throw new RuntimeException("MobileProvider error");
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {

                Optional<DriverProvider> provider = this.readAnnotation(context, DriverProvider.class);
                if (provider.isPresent()) {
                    this.mobileProvider.set(new MobileProvider());
                    this.mobileProvider.get().setMobileConfiguration(new PropertiesManager().getOrCreate(MobileConfiguration.class));
                    this.mobileProvider.get().setAssertionsExtension(new AssertJHandler());
                    CapsReaderAdapter capsReader = this.capsReaderAdapter(provider.get());
                    this.mobileProvider.get().setAppiumServiceManager(new AppiumServiceManager(capsReader.getJsonObject().getNodeJs(), capsReader.getJsonObject().getAppiumExe(), capsReader.getJsonObject().getAppiumIp(), Integer.parseInt(capsReader.getJsonObject().getAppiumPort()), capsReader.getJsonObject().getAndroidHome()));
                    DriverType driverType = this.getDriverType(capsReader.getJsonObject().getClient());
                    this.mobileProvider.get().setDriverManager(new MobileDriverProvider(driverType, capsReader.getCapabilities(), capsReader.getJsonObject().getDriverUrl(), provider.get().implicitlyWait()));
                    ApplicationLaunchOption applicationLaunchOption = provider.get().appLaunchOption();
                    this.mobileProvider.get().getDriverManager().getAppLauncherExtensions().applicationLaunchOptions(applicationLaunchOption, capsReader.getJsonObject().getAppBundleId());
                } else throw new RuntimeException("MobileDriverProviderExtension error DriverProvider is not visible");

            } else throw new RuntimeException("MobileDriverProviderExtension error DriverProvider is not visible");
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext extensionContext)  {
        if (extensionContext.getElement().isPresent()) {
            try {
                if (this.mobileProvider.get().getAppiumServiceManager() != null) {
                    this.mobileProvider.get().getAppiumServiceManager().close();
                }
            } catch (Exception ignore) {}
        }
    }

    private synchronized CapsReaderAdapter capsReaderAdapter(DriverProvider driverProvider) {
        CapsReaderAdapter capsReader = new CapsReaderAdapter(driverProvider.jsonCapabilitiesFile());
        DesiredCapabilities extra = this.setCapabilitiesExtra(capsReader.getJsonObject().getClient(), driverProvider);
        capsReader.getCapabilities().merge(extra);
        return capsReader;
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

    private synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
