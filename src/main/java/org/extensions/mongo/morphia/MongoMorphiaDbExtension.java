package org.extensions.mongo.morphia;

import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.factory.JunitReflectionAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.mongo.morphia.MorphiaRepository;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class MongoMorphiaDbExtension implements AfterAllCallback, ParameterResolver, JunitReflectionAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<MorphiaRepository> repository = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext extensionContext)  {
        return parameter.getParameter().getType() == MorphiaRepository.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext extension)  {
        if (extension.getElement().isPresent()) {
            Optional<MongoMorphiaConnector> mongoConnector = this.readAnnotation(extension, MongoMorphiaConnector.class);
            mongoConnector.ifPresent(connector -> repository.set(new MorphiaRepository(connector.host(), connector.dbName())));
            return this.repository.get();
        }
        return new RuntimeException("Fail init mongo db connection");
    }

    @Override
    public synchronized void afterAll(ExtensionContext extension) {
        if (extension.getElement().isPresent()) {
            Optional<MongoMorphiaConnector> mongoConnector = this.readAnnotation(extension, MongoMorphiaConnector.class);
            mongoConnector.ifPresent(connector -> this.repository.get().close());
        }
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
