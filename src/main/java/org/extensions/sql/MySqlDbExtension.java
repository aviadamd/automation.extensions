package org.extensions.sql;

import com.aventstack.extentreports.Status;
import org.assertj.core.api.Assertions;
import org.extensions.anontations.mySql.MySqlConnector;
import org.junit.jupiter.api.extension.*;
import org.utils.sql.MySqlSharedConnector;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class MySqlDbExtension implements ParameterResolver, AfterAllCallback {
    private final ThreadLocal<MySqlSharedConnector> mySqlRepo = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == MySqlSharedConnector.class;
    }
    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        Optional<MySqlConnector> connector = this.readAnnotation(context, MySqlConnector.class);

        if (connector.isPresent()) {
            this.checkConnectionStrings(connector.get().connection(), connector.get().userName(), connector.get().userPassword());
            this.mySqlRepo.set(new MySqlSharedConnector(connector.get().connection(), connector.get().userName(), connector.get().userPassword()));
            return this.mySqlRepo.get();
        }
        return new RuntimeException("Fail init sql connection");
    }

    private synchronized void checkConnectionStrings(String connection, String user, String pass) {
        Assertions.assertThat(connection == null).as("connection string is not null").isFalse();
        Assertions.assertThat(connection.isEmpty()).isFalse();
        Assertions.assertThat(user == null).isFalse();
        Assertions.assertThat(user.isEmpty()).isFalse();
        Assertions.assertThat(pass == null).isFalse();
        Assertions.assertThat(pass.isEmpty()).isFalse();
    }
    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<MySqlConnector> connector = this.readAnnotation(context, MySqlConnector.class);
            connector.ifPresent(mySqlConnector -> this.mySqlRepo.get().closeConnection(Status.INFO));
        }
    }

    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
