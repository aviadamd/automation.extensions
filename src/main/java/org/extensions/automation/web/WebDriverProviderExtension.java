package org.extensions.automation.web;

import org.automation.web.SeleniumWebDriverManager;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.factory.JunitAnnotationHandler;
import org.files.jsonReader.JacksonWriterExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.*;

public class WebDriverProviderExtension implements ParameterResolver, AfterEachCallback, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final HashMap<Long, SeleniumWebDriverManager> setManager = new HashMap<>();
    private synchronized SeleniumWebDriverManager getManagerInstance() { return this.setManager.get(Thread.currentThread().getId());}
    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == SeleniumWebDriverManager.class;
    }
    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
            if (driverType.isPresent()) {
                for (Class<? extends WebDriver> instance : driverType.get().driversInstance()) {
                    setManager.put(Thread.currentThread().getId(), new SeleniumWebDriverManager(
                            driverType.get().isWithProxy(),
                            System.getProperty(driverType.get().baseUrl()),
                            instance,
                            Duration.ofSeconds(driverType.get().generalTo()))
                    );
                }
            }
            return getManagerInstance();
        } else throw new RuntimeException("WebDriverProviderExtension error");
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    if (getManagerInstance().getBrowserMobProxy() != null && getManagerInstance().getBrowserMobProxy().getHar() != null) {
                        JacksonWriterExtension write = new JacksonWriterExtension(new File(System.getProperty("user.dir") + "/target/" + context.getRequiredTestMethod().getName() + ".json"));
                        write.writeToJson(getManagerInstance().getBrowserMobProxy().getHar(), false);
                    }
                }
            } catch (Exception ignore) {}
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    if (getManagerInstance().getBrowserMobProxy() != null) {
                        getManagerInstance().getBrowserMobProxy().stop();
                    }
                }
            } catch (Exception ignore) {}
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

