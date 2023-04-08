package org.extensions.automation.mobile;

import org.base.mobile.MobileConfiguration;
import org.base.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.mobile.appium.AppiumServerArgumentsInjections;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.utils.mongo.morphia.MorphiaRepository;
import java.util.Optional;

public class MobileSharedObjectsProviderExtension implements ParameterResolver, BeforeEachCallback {

    private final ThreadLocal<MobileConfiguration> mobileProperties = new ThreadLocal<>();
    private final ThreadLocal<MobileSharedObjects<?>> mobileSharedObjects = new ThreadLocal<>();

    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == MobileSharedObjects.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context)  {
        return this.mobileSharedObjects.get();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        this.setMorphia(context);
        this.setDriver(context);
    }

    private void setMorphia(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<MongoMorphiaConnector> mongoConnectorProvider = Optional.ofNullable(context.getElement().get().getAnnotation(MongoMorphiaConnector.class));
                if (mongoConnectorProvider.isPresent()) {
                    MorphiaRepository morphiaRepository = new MorphiaRepository(mongoConnectorProvider.get().host(), mongoConnectorProvider.get().dbName());
                    this.mobileSharedObjects.get().setMorphiaRepository(morphiaRepository);
                }
            }
        } catch (Exception exception) {
            Assertions.fail("set morphia mongo db connection error " + exception.getMessage(), exception);
        }
    }

    private void setDriver(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<DriverProvider> driverJsonProvider = Optional.ofNullable(context.getElement().get().getAnnotation(DriverProvider.class));
            if (driverJsonProvider.isPresent()) {
                String jsonCapsPath = driverJsonProvider.get().jsonCapsPath();
                this.mobileProperties.get().setProperty("android.caps.json", jsonCapsPath);
                AppiumServerArgumentsInjections argumentsInjections = context.getElement().get().getAnnotation(AppiumServerArgumentsInjections.class);
                CapsReaderAdapter capsReaderAdapter = this.capsReaderAdapter(argumentsInjections);
                this.mobileSharedObjects.get().setDriverManager(new MobileDriverProvider(capsReaderAdapter));
            } else throw new RuntimeException("fail resolve driver provider initiation");
        }
    }
    private CapsReaderAdapter capsReaderAdapter(AppiumServerArgumentsInjections serverArguments) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        CapsReaderAdapter capsReaderAdapter = new CapsReaderAdapter(this.mobileProperties.get().mobileJsonCapabilitiesLocation());
        if (serverArguments != null) desiredCapabilities.merge(this.setCapabilitiesExtra(capsReaderAdapter, serverArguments));
        desiredCapabilities.merge(capsReaderAdapter.getCapabilities());
        return capsReaderAdapter;
    }

    private DesiredCapabilities setCapabilitiesExtra(CapsReaderAdapter capsAdapter, AppiumServerArgumentsInjections arguments) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        String clientType = capsAdapter.getJsonObject().getClient();
        switch (clientType) {
            case "ANDROID" -> {
                if (arguments.android() != null && arguments.android().keys().length > 0) {
                    desiredCapabilities.merge(capsAdapter.setDesiredCapabilities(arguments.android().keys(), arguments.android().values()));
                }
            }
            case "IOS" -> {
                if (arguments.ios() != null && arguments.ios().keys().length > 0) {
                    desiredCapabilities.merge(capsAdapter.setDesiredCapabilities(arguments.ios().keys(), arguments.ios().values()));
                }
            }
            default -> throw new RuntimeException("no android or ios driver name was provided");
        }

        return desiredCapabilities;
    }
}
