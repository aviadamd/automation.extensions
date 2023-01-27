package org.extensions.mongo;

import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.mongo.MongoMorphiaManager;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mongo.morphia.MorphiaRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoMorphiaDbExtension implements BeforeEachCallback, AfterAllCallback {
    private final static Map<Integer, MorphiaRepository> repository = new HashMap<>();
    public Map<Integer, MorphiaRepository> getRepository() { return repository; }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoMorphiaManager> mongoManager = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MongoMorphiaManager.class));
            mongoManager.ifPresent(mongoConnector -> {
                for (MongoMorphiaConnector mongoConnection: mongoManager.get().mongoConnectors()) {
                    repository.put(mongoConnection.dbId(), new MorphiaRepository(mongoManager.get().host(), mongoConnection.dbName()));
                }
            });
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoMorphiaManager> mongoManager = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MongoMorphiaManager.class));

        }
    }

}
