package org.extensions.automation.web;

import org.automation.base.web.SeleniumWebDriverManager;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.WebDriver;
import java.time.Duration;
import java.util.HashMap;
import java.util.Optional;

public class WebDriverProviderExtension extends ExtentReportExtension implements ParameterResolver {

    private static final HashMap<Long, SeleniumWebDriverManager> manager = new HashMap<>();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        return parameterContext.getParameter().getType() == SeleniumWebDriverManager.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        try {
            if (context.getElement().isPresent() && parameterContext.getParameter().getType() == SeleniumWebDriverManager.class) {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    for (Class<? extends WebDriver> instance : driverType.get().driversInstance()) {
                        manager.put(Thread.currentThread().getId(), new SeleniumWebDriverManager(Thread.currentThread().getId(), instance, Duration.ofSeconds(driverType.get().durationTimeOuts())));
                    }
                }
                return manager.get(Thread.currentThread().getId());
            } else throw new RuntimeException("WebDriverProviderExtension error");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}

