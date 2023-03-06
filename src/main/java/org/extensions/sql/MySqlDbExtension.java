package org.extensions.sql;

import org.extensions.anontations.mySql.MySqlConnector;
import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.sql.MySqlSharedConnector;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class MySqlDbExtension implements ParameterResolver, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<MySqlSharedConnector> mySqlRepo = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == MySqlSharedConnector.class;
    }
    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        Optional<MySqlConnector> connector = this.readAnnotation(context, MySqlConnector.class);
        if (connector.isPresent()) {
            this.mySqlRepo.set(new MySqlSharedConnector(
                    connector.get().connection(),
                    connector.get().userName(),
                    connector.get().userPassword()));
            return this.mySqlRepo.get();
        }
        return new RuntimeException("Fail init sql connection");
    }
    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<MySqlConnector> connector = this.readAnnotation(context, MySqlConnector.class);
            connector.ifPresent(mySqlConnector -> this.mySqlRepo.get().closeConnection());
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
