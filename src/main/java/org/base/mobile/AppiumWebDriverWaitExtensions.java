package org.base.mobile;

import io.appium.java_client.AppiumFluentWait;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import java.util.Arrays;
import java.util.Collection;

public class AppiumWebDriverWaitExtensions {
    private final ThreadLocal<AppiumFluentWait<WebDriver>> webDriverWait = new ThreadLocal<>();
    public AppiumFluentWait<WebDriver> getWebDriverWait() { return this.webDriverWait.get(); }

    public AppiumWebDriverWaitExtensions(MobileDriverProvider mobileDriverProvider) {
        this.webDriverWait.set(new AppiumFluentWait<>(mobileDriverProvider.getMobileDriver()));
    }

}
