package org.base.mobile;

import io.appium.java_client.AppiumFluentWait;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import java.util.Arrays;
import java.util.Collection;

public class AppiumWebDriverWaitExtensions {
    private final MobileDriverProvider mobileDriverProvider;
    private final ThreadLocal<AppiumFluentWait<WebDriver>> webDriverWait = new ThreadLocal<>();
    public AppiumFluentWait<WebDriver> getWebDriverWait() { return this.webDriverWait.get(); }

    public MobileDriverProvider getMobileDriverProvider() { return this.mobileDriverProvider; }

    public AppiumWebDriverWaitExtensions(MobileDriverProvider mobileDriverProvider) {
        this.mobileDriverProvider = mobileDriverProvider;
        this.webDriverWait.set(new AppiumFluentWait<>(mobileDriverProvider.getMobileDriver()));
    }

    public FluentWait<WebDriver> optionalFluentWait() {
        return this.webDriverWait
                .get()
                .ignoreAll(this.commonExceptions());
    }

    public WebDriver optionalFindElement(ExpectedCondition<WebDriver> expectedConditions) {
        return this.webDriverWait
                .get()
                .ignoreAll(this.commonExceptions())
                .until(expectedConditions);
    }

    public By optionalFindElementBy(ExpectedCondition<By> expectedConditions) {
        return this.webDriverWait
                .get()
                .ignoreAll(this.commonExceptions())
                .until(expectedConditions);
    }

    public Collection<Class<? extends Exception>> commonExceptions() {
        return Arrays.asList(
                Exception.class,
                WebDriverException.class,
                NoSuchElementException.class,
                StaleElementReferenceException.class
        );
    }
}
