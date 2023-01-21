package org.extensions.sql;

import org.extensions.anontations.mySql.MySqlConnector;
import org.extensions.anontations.mySql.MySqlConnectorManager;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.mySql.MySqlSharedConnector;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.util.HashMap;
import java.util.Optional;
public class MySqlDbExtension implements BeforeAllCallback, AfterAllCallback {
    public static HashMap<Integer, MySqlSharedConnector> mySqlRepo = new HashMap<>();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnectorManager> connector = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MySqlConnectorManager.class));
            connector.ifPresent(db -> {
                for (MySqlConnector sqlConnector: db.connector()) {
                    mySqlRepo.put(sqlConnector.dbId(), new MySqlSharedConnector(sqlConnector.connection(), sqlConnector.userName(), sqlConnector.userPassword()));
                }
            });
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnectorManager> connector = Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MySqlConnectorManager.class));
            connector.ifPresent(db -> {
                for (MySqlConnector sqlConnector: db.connector()) {
                    mySqlRepo.get(sqlConnector.dbId()).step(MySqlSharedConnector.SharedQuery::closeConnection);
                }
            });
        }
    }
}
