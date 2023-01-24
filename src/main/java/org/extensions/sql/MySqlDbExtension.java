package org.extensions.sql;

import org.extensions.anontations.mySql.MySqlConnector;
import org.extensions.anontations.mySql.MySqlConnectorManager;
import org.junit.jupiter.api.extension.*;
import org.mySql.MySqlSharedConnector;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
@Component
public class MySqlDbExtension implements BeforeEachCallback, BeforeTestExecutionCallback, AfterAllCallback {
    private final static HashMap<Integer, MySqlSharedConnector> mySqlRepo = new HashMap<>();
    public HashMap<Integer, MySqlSharedConnector> getMySqlRepo() { return mySqlRepo; }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnectorManager> connector = this.connectorManager(extensionContext);
            connector.ifPresent(db -> {
                for (MySqlConnector sqlConnector: db.connector()) {
                    mySqlRepo.put(sqlConnector.dbId(), new MySqlSharedConnector(sqlConnector.connection(), sqlConnector.userName(), sqlConnector.userPassword()));
                }
            });
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnectorManager> connector = this.connectorManager(extensionContext);
            connector.ifPresent(db -> {
                for (MySqlConnector sqlConnector: db.connector()) {
                    if (!mySqlRepo.containsKey(sqlConnector.dbId())) {
                        mySqlRepo.put(sqlConnector.dbId(), new MySqlSharedConnector(sqlConnector.connection(), sqlConnector.userName(), sqlConnector.userPassword()));
                    }
                }
            });
        }
    }
    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnectorManager> connector = this.connectorManager(extensionContext);
            connector.ifPresent(db -> {
                for (MySqlConnector sqlConnector: db.connector()) {
                    mySqlRepo.get(sqlConnector.dbId()).step(MySqlSharedConnector.SharedQuery::closeConnection);
                }
            });
        }
    }

    private Optional<MySqlConnectorManager> connectorManager(ExtensionContext extensionContext) {
        return Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MySqlConnectorManager.class));
    }
}
