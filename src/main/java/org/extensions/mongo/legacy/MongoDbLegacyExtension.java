package org.extensions.mongo.legacy;

import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.mongo.legacy.MongoRepoImplementation;
import org.extensions.anontations.mongo.MongoLegacyConnector;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Optional;

public class MongoDbLegacyExtension implements ParameterResolver, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final HashMap<Long, MongoRepoImplementation> mongoRepo = new HashMap<>();
    private synchronized MongoRepoImplementation getMongoRepo() { return this.mongoRepo.get(Thread.currentThread().getId()); }

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)  {
        return parameterContext.getParameter().getType() == MongoRepoImplementation.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)  {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoLegacyConnector> connector = this.readAnnotation(extensionContext, MongoLegacyConnector.class);
            connector.ifPresent(mongoConnector -> mongoRepo.put(Thread.currentThread().getId(), new MongoRepoImplementation(mongoConnector.host(), mongoConnector.dbName(), mongoConnector.collectionName())));
            return mongoRepo.get(Thread.currentThread().getId());
        }
        throw new RuntimeException("Fail init mongoConnector");
    }

    @Override
    public synchronized void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoLegacyConnector> connector = this.readAnnotation(extensionContext, MongoLegacyConnector.class);
            connector.ifPresent(mongoConnector -> this.getMongoRepo().close());
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
