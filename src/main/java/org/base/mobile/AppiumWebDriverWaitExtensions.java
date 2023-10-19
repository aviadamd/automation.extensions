package org.base.mobile;

import io.appium.java_client.AppiumFluentWait;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import java.util.Arrays;
import java.util.Collection;

public class AppiumWebDriverWaitExtensions {
    private final ThreadLocal<AppiumFluentWait<WebDriver>> webDriverWait = new ThreadLocal<>();
    public final AppiumFluentWait<WebDriver> getWebDriverWait() { return this.webDriverWait.get(); }

    public AppiumWebDriverWaitExtensions(WebDriver webDriver) {
        this.webDriverWait.set(new AppiumFluentWait<>(webDriver));
    }

    public Collection<Class<? extends Exception>> sessionExceptions() {
        return Arrays.asList(
                Exception.class,
                WebDriverException.class,
                NoSuchElementException.class,
                StaleElementReferenceException.class
        );
    }
}
