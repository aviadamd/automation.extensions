package org.base.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.base.WebElementGestures;
import org.base.mobile.MobileDriverActions;
import org.extensions.automation.WebDriverEventHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.*;

@Slf4j
public class SeleniumWebDriverProvider implements WebDriver, WebElementGestures {
    private Duration generalTimeOut = Duration.ofSeconds(5);
    private Duration pollingEvery = Duration.ofSeconds(1);
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<WebDriverWait> webDriverWait = new ThreadLocal<>();
    private final ThreadLocal<WebDriverScrollExtension> scrollExtension = new ThreadLocal<>();

    public Collection<Class<? extends Exception>> findElementExceptions() {
        return new ArrayList<>(Arrays.asList(
                Exception.class,
                WebDriverException.class,
                NoSuchElementException.class
        ));
    }
    public WebDriver getDriver() { return driver.get(); }
    public WebDriverWait getWebDriverWait() { return this.webDriverWait.get(); }
    public WebDriverScrollExtension getScrollExtension() { return this.scrollExtension.get(); }

    public SeleniumWebDriverProvider(String baseUrl, Duration duration, WebDriver webDriver) {
        WebDriverEventHandler eventHandler = new WebDriverEventHandler(webDriver);
        this.driver.set(new EventFiringDecorator<>(eventHandler).decorate(webDriver));
        this.webDriverWait.set(new WebDriverWait(this.driver.get(), duration));
        if (!baseUrl.isEmpty()) this.get(baseUrl);
        this.scrollExtension.set(new WebDriverScrollExtension(this));
    }
    public SeleniumWebDriverProvider(String baseUrl, Class<? extends WebDriver> driverInstance, Duration duration) {
        this.driver.set(WebDriverManager.getInstance(driverInstance).create());
        this.webDriverWait.set(new WebDriverWait(this.driver.get(), duration));
        if (!baseUrl.isEmpty()) this.get(baseUrl);
        this.scrollExtension.set(new WebDriverScrollExtension(this));
    }

    public SeleniumWebDriverProvider oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    @Override
    public void get(String url) {this.getDriver().get(url);}
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
        try {
            if (this.driver.get() != null) this.getDriver().close();
        } catch (Exception ignore) {}
    }
    @Override
    public void quit() {
        try {
            if (this.driver.get() != null) this.getDriver().quit();
        } catch (Exception ignore) {}
    }
    @Override
    public void click(WebElement element) {
        this.getWebDriverWait()
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(element)).click();
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
