package org.extensions;

import org.extensions.anontations.mongo.MongoManager;
import org.mongo.MongoCollectionImplementation;
import org.mongo.MongoConnection;
import org.extensions.anontations.mongo.MongoConnector;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoDbExtension implements BeforeEachCallback, AfterAllCallback {
    public static Map<Integer,MongoCollectionImplementation> mongoRepo = new HashMap<>();

    @Override
    public void beforeEach(ExtensionContext extensionContext)  {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoManager> mongoManager = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MongoManager.class));
            mongoManager.ifPresent(mongoConnector -> {
                for (MongoConnector mongoConnection: mongoManager.get().mongoConnectors()) {
                    mongoRepo.put(mongoConnection.dbId(), new MongoCollectionImplementation(new MongoConnection(mongoManager.get().host(), mongoConnection.dbName()), mongoConnection.collectionName()));
                }
            });
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MongoManager> mongoManager = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MongoManager.class));
            mongoManager.ifPresent(mongoConnector -> {
                for (MongoConnector mongoConnection: mongoManager.get().mongoConnectors()) {
                    mongoRepo.get(mongoConnection.dbId()).close();
                }
            });
        }
    }
}
