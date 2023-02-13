package org.automation.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.automation.WebElementGestures;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.*;

@Augmentable
public class SeleniumWebDriverManager implements WebDriver, WebElementGestures {
    private Duration generalTimeOut = Duration.ofSeconds(5);
    private Duration pollingEvery = Duration.ofSeconds(1);
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<WebDriverWait> webDriverWait = new ThreadLocal<>();
    public SeleniumWebDriverManager(String baseUrl, Duration duration, WebDriver webDriver) {
        this.driver.set(webDriver);
        this.webDriverWait.set(new WebDriverWait(this.getDriver(), duration));
        if (!baseUrl.isEmpty()) this.get(baseUrl);
    }
    public SeleniumWebDriverManager(String baseUrl, Class<? extends WebDriver> driverInstance, Duration duration) {
        this.driver.set(WebDriverManager.getInstance(driverInstance).create());
        this.webDriverWait.set(new WebDriverWait(this.getDriver(), duration));
        if (!baseUrl.isEmpty()) this.get(baseUrl);
    }

    public WebDriver getDriver() { return driver.get(); }
    public WebDriverWait getWebDriverWait() { return this.webDriverWait.get(); }

    public SeleniumWebDriverManager oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    @Override
    public void get(String url) {
        this.getDriver().get(url);
    }
    @Override
    public String getCurrentUrl() {
        return this.getWebDriverWait().until(WebDriver::getCurrentUrl);
    }
    @Override
    public String getTitle() {
        return this.getWebDriverWait().until(WebDriver::getTitle);
    }
    @Override
    public Options manage() {
        return this.getDriver().manage();
    }
    @Override
    public TargetLocator switchTo() {
        return this.getDriver().switchTo();
    }
    @Override
    public Navigation navigate() {
        return this.getDriver().navigate();
    }
    @Override
    public Set<String> getWindowHandles() {
        return this.getDriver().getWindowHandles();
    }
    @Override
    public String getWindowHandle() {
        return this.getDriver().getWindowHandle();
    }
    @Override
    public void close() {
        this.getDriver().close();
    }
    @Override
    public void quit() {
        this.getDriver().quit();
    }
    @Override
    public void click(WebElement element) {
        element.click();
    }
    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition) {
        this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .click();
    }
    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition, Duration generalTimeOut, Duration pollingEvery) {
        this.getWebDriverWait()
                .withTimeout(generalTimeOut)
                .pollingEvery(pollingEvery)
                .until(expectedCondition)
                .click();
    }

    @Override
    public void sendKeys(WebElement element, CharSequence... keysToSend) {
        this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(keysToSend);
    }

    @Override
    public void sendKeys(ExpectedCondition<WebElement> expectedCondition, CharSequence... keysToSend) {
        this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .sendKeys(keysToSend);
    }
    @Override
    public String getAttribute(WebElement element, String name) {
        return this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getAttribute(name));
    }

    @Override
    public String getText(WebElement element) {
        return this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getText());
    }

    @Override
    public List<WebElement> findElements(By by) {
        return this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElements(by));
    }
    @Override
    public WebElement findElement(By by) {
        return this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(by));
    }
    @Override
    public String getPageSource() {
        return this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(WebDriver::getPageSource);
    }

}
