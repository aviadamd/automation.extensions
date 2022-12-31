package org.extensions;

import org.extensions.anontations.MySqlConnector;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.mySql.MySqlSharedConnector;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.util.HashMap;
import java.util.Optional;
public class MySqlDbExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback {
    public static HashMap<Integer, MySqlSharedConnector> mySqlRepo = new HashMap<>();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnector> connector = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MySqlConnector.class));
            connector.ifPresent(mongo -> {
                mySqlRepo.put(mongo.dbId(), new MySqlSharedConnector(mongo.connection(), mongo.userName(), mongo.userPassword()));
            });
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnector> connector = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MySqlConnector.class));
            connector.ifPresent(mongo -> {
                if (!mySqlRepo.containsKey(mongo.dbId())) {
                    mySqlRepo.put(mongo.dbId(), new MySqlSharedConnector(mongo.connection(), mongo.userName(), mongo.userPassword()));
                }
            });
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnector> connector = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MySqlConnector.class));
            connector.ifPresent(mongo -> {
                mySqlRepo.get(mongo.dbId()).step(MySqlSharedConnector.SharedQuery::closeConnection);
            });
        }
    }
}
