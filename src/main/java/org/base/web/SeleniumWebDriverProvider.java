package org.base.web;

import lombok.extern.slf4j.Slf4j;
import org.base.anontations.WebElementGestures;
import org.extensions.web.WebDriverListenerImpl;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v110.browser.Browser;
import org.openqa.selenium.devtools.v110.log.Log;
import org.openqa.selenium.devtools.v110.network.Network;
import org.openqa.selenium.devtools.v110.runtime.Runtime;
import org.openqa.selenium.devtools.v110.security.Security;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.*;

@Slf4j
public class SeleniumWebDriverProvider implements WebDriver, WebElementGestures {
    private Duration generalTimeOut = Duration.ofSeconds(5);
    private Duration pollingEvery = Duration.ofSeconds(1);
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<WebDriverWaitExtensions> waitExtensions = new ThreadLocal<>();
    private final ThreadLocal<WebDriverScrollExtension> scrollExtension = new ThreadLocal<>();
    public WebDriver getDriver() { return driver.get(); }
    public WebDriverWaitExtensions getWaitExtensions() { return this.waitExtensions.get(); }
    public WebDriverScrollExtension getScrollExtension() { return this.scrollExtension.get(); }

    public SeleniumWebDriverProvider(SeleniumWebDriverManager driverManager) {
        this.driver.set(new EventFiringDecorator<>(new WebDriverListenerImpl()).decorate(driverManager.getDriver()));
       // this.addDriverDevTools(this.getDriver());
        this.waitExtensions.set(new WebDriverWaitExtensions(this, driverManager.getDuration()));
        this.scrollExtension.set(new WebDriverScrollExtension(this));
    }

    public SeleniumWebDriverProvider(Duration duration, WebDriver webDriver) {
        this.driver.set(new EventFiringDecorator<>(new WebDriverListenerImpl()).decorate(webDriver));
        this.waitExtensions.set(new WebDriverWaitExtensions(this, duration));
        this.scrollExtension.set(new WebDriverScrollExtension(this));
    }

    public SeleniumWebDriverProvider oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    private synchronized void addDriverDevTools(WebDriver driver) {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.send(Network.enable(Optional.of(100000000), Optional.empty(), Optional.empty()));
        devTools.createSession(driver.getWindowHandle());

        devTools.send(Browser.getVersion());
        devTools.send(Log.enable());
        devTools.send(Runtime.enable());
        devTools.send(Network.enableReportingApi(true));
        devTools.send(Security.setIgnoreCertificateErrors(true));
        devTools.send(Browser.getBrowserCommandLine());
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.responseReceived(), requestSent -> {
            log.debug("------------------------------------------------------");
            log.debug("URL => " + requestSent.getResponse().getUrl());
            log.debug("Status => " + requestSent.getResponse().getStatus());
            log.debug("Headers => " + requestSent.getResponse().getHeaders().toString());
            log.debug("------------------------------------------------------");
        });

    }

    @Override
    public void get(String url) {
        this.driver.get().get(url);
    }

    @Override
    public String getCurrentUrl() {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .until(WebDriver::getCurrentUrl);
    }

    @Override
    public String getTitle() {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .until(WebDriver::getTitle);
    }

    @Override
    public Options manage() {
        return this.driver.get().manage();
    }

    @Override
    public TargetLocator switchTo() {
        return this.driver.get().switchTo();
    }

    @Override
    public Navigation navigate() {
        return this.driver.get().navigate();
    }

    @Override
    public Set<String> getWindowHandles() {
        return this.driver.get().getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return this.driver.get().getWindowHandle();
    }

    @Override
    public void close() {
        try {
            if (this.driver.get() != null) {
                this.driver.get().close();
            }
        } catch (Exception exception) {
            Assertions.fail("close driver error ", exception);
        }
    }

    @Override
    public void quit() {
        try {
            if (this.driver.get() != null) {
                this.driver.get().quit();
            }
        } catch (Exception exception) {
            Assertions.fail("quit driver error ", exception);
        }
    }

    @Override
    public void click(WebElement element) {
        this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition) {
        this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .click();
    }

    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition, Duration generalTimeOut, Duration pollingEvery) {
        this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(generalTimeOut)
                .pollingEvery(pollingEvery)
                .until(expectedCondition)
                .click();
    }

    @Override
    public void sendKeys(WebElement element, CharSequence... keysToSend) {
        this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(keysToSend);
    }

    @Override
    public void sendKeys(ExpectedCondition<WebElement> expectedCondition, CharSequence... keysToSend) {
        this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .sendKeys(keysToSend);
    }

    @Override
    public String getAttribute(WebElement element, String name) {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getAttribute(name));
    }

    @Override
    public String getText(WebElement element) {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getText());
    }

    @Override
    public List<WebElement> findElements(By by) {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElements(by));
    }

    @Override
    public List<WebElement> findElements(By byFather, By son) {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(byFather).findElements(son));
    }

    @Override
    public WebElement findElement(By by) {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(by));
    }

    @Override
    public WebElement findElement(By byFather, By son) {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(byFather).findElement(son));
    }

    @Override
    public WebElement findElement(ExpectedCondition<WebElement> expectedConditions) {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedConditions);
    }

    @Override
    public String getPageSource() {
        return this.waitExtensions
                .get()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(WebDriver::getPageSource);
    }
}
