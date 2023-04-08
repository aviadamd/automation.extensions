package org.base.web;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

public class WebDriverWaitExtensions {
    private final SeleniumWebDriverProvider seleniumWebDriverProvider;
    private final ThreadLocal<WebDriverWait> webDriverWait = new ThreadLocal<>();
    public WebDriverWait getWebDriverWait() { return this.webDriverWait.get(); }

    public SeleniumWebDriverProvider getSeleniumWebDriverProvider() { return this.seleniumWebDriverProvider; }

    public WebDriverWaitExtensions(SeleniumWebDriverProvider seleniumWebDriverProvider, Duration duration) {
        this.seleniumWebDriverProvider = seleniumWebDriverProvider;
        this.webDriverWait.set(new WebDriverWait(seleniumWebDriverProvider.getDriver(), duration));
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
