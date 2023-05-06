package org.extensions.jackson;

import org.extensions.anontations.JacksonProvider;
import org.data.files.jsonReader.JacksonObjectAdapter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import java.io.File;
import java.util.Optional;

public class JacksonProviderExtension implements ParameterResolver {
    private final ThreadLocal<JacksonObjectAdapter<?>> jacksonHelper = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == JacksonObjectAdapter.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<JacksonProvider> provider = Optional.ofNullable(context.getElement().get().getAnnotation(JacksonProvider.class));
                if (provider.isPresent()) {
                    String path = System.getProperty("user.dir") + "/" + provider.get().dir();
                    this.jacksonHelper.set(new JacksonObjectAdapter<>(path, new File(path + "/" + provider.get().fileName()), provider.get().classObject()));
                    return this.jacksonHelper.get();
                }
            } catch (Exception exception) {
                throw new RuntimeException("fail resolve jackson provider initiation", exception);
            }
        } throw new RuntimeException("fail resolve jackson provider initiation");
    }
}
