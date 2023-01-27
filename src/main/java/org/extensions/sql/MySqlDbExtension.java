package org.extensions.sql;

import org.extensions.anontations.mySql.MySqlConnector;
import org.extensions.anontations.mySql.MySqlConnectorManager;
import org.junit.jupiter.api.extension.*;
import org.mySql.MySqlSharedConnector;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class MySqlDbExtension implements BeforeEachCallback, BeforeTestExecutionCallback, AfterAllCallback {
    private static final ThreadLocal<Map<Integer, MySqlSharedConnector>> mySqlRepo = new ThreadLocal<>();
    public Map<Integer, MySqlSharedConnector>  getMySqlRepo() { return mySqlRepo.get(); }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnectorManager> connector = this.connectorManager(extensionContext);
            connector.ifPresent(db -> {
                for (MySqlConnector sqlConnector: db.connector()) {
                    mySqlRepo.set(Map.of(sqlConnector.dbId(), new MySqlSharedConnector(sqlConnector.connection(), sqlConnector.userName(), sqlConnector.userPassword())));
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
                    mySqlRepo.set(Map.of(sqlConnector.dbId(), new MySqlSharedConnector(sqlConnector.connection(), sqlConnector.userName(), sqlConnector.userPassword())));
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
                    mySqlRepo.get().get(sqlConnector.dbId()).step(MySqlSharedConnector.SharedQuery::closeConnection);
                }
                mySqlRepo.remove();
            });
        }
    }

    private Optional<MySqlConnectorManager> connectorManager(ExtensionContext extensionContext) {
        return Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(MySqlConnectorManager.class));
    }
}
