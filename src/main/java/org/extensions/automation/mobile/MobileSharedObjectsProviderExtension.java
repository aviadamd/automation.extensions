package org.extensions.automation.mobile;

import org.base.mobile.MobileConfiguration;
import org.base.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.utils.mongo.morphia.MorphiaRepository;

import java.util.Optional;

public class MobileSharedObjectsProviderExtension implements ParameterResolver {
    private final ThreadLocal<MorphiaRepository> repository = new ThreadLocal<>();
    private final ThreadLocal<MobileDriverProvider> driverManager = new ThreadLocal<>();
    private final ThreadLocal<MobileConfiguration> mobileProperties = new ThreadLocal<>();

    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext extension) {
        return parameter.getParameter().getType() == MobileSharedObjects.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context)  {
        if (context.getElement().isPresent()) {

            try {

                Optional<MongoMorphiaConnector> mongoConnectorProvider = Optional.ofNullable(context.getElement().get().getAnnotation(MongoMorphiaConnector.class));
                mongoConnectorProvider.ifPresent(provider -> repository.set(new MorphiaRepository(provider.host(), provider.dbName())));

                Optional<DriverProvider> driverJsonProvider = Optional.ofNullable(context.getElement().get().getAnnotation(DriverProvider.class));
                driverJsonProvider.ifPresent(provider -> this.mobileProperties.get().setProperty("android.caps.json", driverJsonProvider.get().jsonCapsPath()));
                this.driverManager.set(new MobileDriverProvider(new CapsReaderAdapter(this.mobileProperties.get().mobileJsonCapabilities())));

                return new MobileSharedObjects(this.driverManager.get(), this.repository.get());
            } catch (Exception exception) {
                throw new RuntimeException("fail resolve jackson provider initiation", exception);
            }

        } throw new RuntimeException("fail resolve jackson provider initiation");
    }
}
