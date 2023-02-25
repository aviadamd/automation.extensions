package org.extensions.automation.mobile;

import org.automation.mobile.MobileConfiguration;
import org.automation.mobile.MobileDriverProvider;
import org.extensions.anontations.JacksonProvider;
import org.extensions.anontations.mobile.DriverJsonProvider;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mongo.morphia.MorphiaRepository;

import java.io.File;
import java.util.Optional;

public class MobileSharedObjectsProviderExtension implements ParameterResolver {
    private final ThreadLocal<MorphiaRepository> repository = new ThreadLocal<>();
    private final ThreadLocal<JacksonExtension<?>> jacksonHelper = new ThreadLocal<>();
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

                Optional<JacksonProvider> jacksonProvider = Optional.ofNullable(context.getElement().get().getAnnotation(JacksonProvider.class));
                jacksonProvider.ifPresent(provider -> {
                    String path = System.getProperty("user.dir") + "/" + jacksonProvider.get().dir();
                    this.jacksonHelper.set(new JacksonExtension<>(path, new File(path + "/" + jacksonProvider.get().fileName()) , jacksonProvider.get().classObject()));
                });

                Optional<MongoMorphiaConnector> mongoConnectorProvider = Optional.ofNullable(context.getElement().get().getAnnotation(MongoMorphiaConnector.class));
                mongoConnectorProvider.ifPresent(provider -> repository.set(new MorphiaRepository(provider.host(), provider.dbName())));

                Optional<DriverJsonProvider> driverJsonProvider = Optional.ofNullable(context.getElement().get().getAnnotation(DriverJsonProvider.class));
                driverJsonProvider.ifPresent(provider -> this.mobileProperties.get().setProperty("android.caps.json", driverJsonProvider.get().jsonCapsPath()));
                this.driverManager.set(new MobileDriverProvider(new CapsReaderAdapter(this.mobileProperties.get().mobileJsonCapabilities())));

                return new MobileSharedObjects(this.driverManager.get(), this.jacksonHelper.get(), this.repository.get());
            } catch (Exception exception) {
                throw new RuntimeException("fail resolve jackson provider initiation", exception);
            }

        } throw new RuntimeException("fail resolve jackson provider initiation");
    }
}
