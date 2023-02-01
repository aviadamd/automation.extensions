package org.extensions.sql;

import org.extensions.anontations.mySql.MySqlConnector;
import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.mySql.MySqlSharedConnector;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Optional;

public class MySqlDbExtension implements ParameterResolver, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final HashMap<Long, MySqlSharedConnector> mySqlRepo = new HashMap<>();
    public synchronized MySqlSharedConnector getMySqlRepo() { return mySqlRepo.get(Thread.currentThread().getId()); }

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == MySqlSharedConnector.class;
    }
    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Optional<MySqlConnector> connector = this.readAnnotation(extensionContext, MySqlConnector.class);
        if (connector.isPresent()) {
            this.mySqlRepo.put(Thread.currentThread().getId(), new MySqlSharedConnector(
                    connector.get().connection(),
                    connector.get().userName(),
                    connector.get().userPassword()));
            return this.getMySqlRepo();
        }
        return new RuntimeException("Fail init sql connection");
    }

    @Override
    public synchronized void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            Optional<MySqlConnector> connector = this.readAnnotation(extensionContext, MySqlConnector.class);
            connector.ifPresent(mySqlConnector -> this.getMySqlRepo().closeConnection());
        }
    }

    @Override
    public <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
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
