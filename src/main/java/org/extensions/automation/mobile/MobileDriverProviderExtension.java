package org.extensions.automation.mobile;

import org.automation.mobile.MobileDriverManager;
import org.extensions.anontations.mobile.DriverJsonProvider;
import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Optional;

public class MobileDriverProviderExtension implements ParameterResolver, JunitAnnotationHandler.ExtensionContextHandler {

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        return parameterContext.getParameter().getType() == MobileDriverManager.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        HashMap<Long, MobileDriverManager> manager = new HashMap<>();
        if (context.getElement().isPresent() && parameterContext.getParameter().getType() == MobileDriverManager.class) {
            Optional<DriverJsonProvider> provider = this.readAnnotation(context, DriverJsonProvider.class);
            if (provider.isPresent()) {
                manager.put(Thread.currentThread().getId(), new MobileDriverManager(Thread.currentThread().getId(), new CapsReaderAdapter(System.getProperty(provider.get().jsonCapsPath()))));
                return manager.get(Thread.currentThread().getId());
            }
        }
        throw new RuntimeException("MobileDriverProviderExtension error DriverJsonProvider");
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
