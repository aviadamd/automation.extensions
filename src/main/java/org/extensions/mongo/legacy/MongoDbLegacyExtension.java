package org.extensions.mongo.legacy;

import org.extensions.factory.JunitReflectionAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.mongo.legacy.MongoRepoImplementation;
import org.extensions.anontations.mongo.MongoLegacyConnector;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class MongoDbLegacyExtension implements ParameterResolver, AfterAllCallback, JunitReflectionAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<MongoRepoImplementation> mongoRepo = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)  {
        return parameterContext.getParameter().getType() == MongoRepoImplementation.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)  {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoLegacyConnector> connector = this.readAnnotation(extensionContext, MongoLegacyConnector.class);
            connector.ifPresent(mongoConnector -> mongoRepo.set(new MongoRepoImplementation(mongoConnector.host(), mongoConnector.dbName(), mongoConnector.collectionName())));
            return mongoRepo.get();
        }
        throw new RuntimeException("Fail init mongoConnector");
    }

    @Override
    public synchronized void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoLegacyConnector> connector = this.readAnnotation(extensionContext, MongoLegacyConnector.class);
            connector.ifPresent(mongoConnector -> this.mongoRepo.get().close());
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
