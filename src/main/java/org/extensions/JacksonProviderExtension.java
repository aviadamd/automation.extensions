package org.extensions;

import org.extensions.anontations.JacksonProvider;
import org.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import java.util.Optional;

public class JacksonProviderExtension implements ParameterResolver {

    private final ThreadLocal<JacksonExtension<?>> jacksonHelper = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == JacksonProviderExtension.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<JacksonProvider> provider = Optional.ofNullable(context.getElement().get().getAnnotation(JacksonProvider.class));
            if (provider.isPresent()) {
                final String path = System.getProperty("user.dir") + "/" + provider.get().dir();
                this.jacksonHelper.set(new JacksonExtension<>(path, provider.get().fileName() , provider.get().classObject()));
                return this.jacksonHelper.get();
            } else throw new RuntimeException("fail resolve jackson provider initiation");
        }
        throw new RuntimeException("fail resolve jackson provider initiation");
    }
}
