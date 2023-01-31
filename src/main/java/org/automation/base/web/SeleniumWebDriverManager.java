package org.automation.base.web;

import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.automation.base.WebElementGestures;
import org.extensions.automation.DriverEventListener;
import org.extensions.web.DriverType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SeleniumWebDriverManager implements WebDriver, WebElementGestures {
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<WebDriverWait> webDriverWait = new ThreadLocal<>();
    public WebDriver getDriver() { return driver.get(); }
    public WebDriverWait getWebDriverWait() { return this.webDriverWait.get(); }
    private Duration generalTimeOut = Duration.ofSeconds(5);
    private Duration pollingEvery = Duration.ofSeconds(1);

    private final HashMap<Long, EventFiringDecorator<WebDriver>> webDriverDecorator = new HashMap<>();

    public EventFiringDecorator<WebDriver> getDriverDecorator() {
        return this.webDriverDecorator.get(Thread.currentThread().getId());
    }

    public SeleniumWebDriverManager oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    /**
     * WebDriverManagerExtension constructor
     * @param driverInstance pass as args
     * @param driverListener your class that implements WebDriverListener
     * @param driverWaitDuration to web driver wait
     */
    public SeleniumWebDriverManager(Class<? extends WebDriver> driverInstance, WebDriverListener driverListener, Duration driverWaitDuration) {
        try {
            WebDriverManager instance = this.manager(driverInstance);
            this.driver.set(new EventFiringDecorator<>(WebDriver.class, driverListener).decorate(instance.clearDriverCache().create()));
            this.webDriverWait.set(new WebDriverWait(this.driver.get(), driverWaitDuration));
        } catch (Exception exception) {
            Assertions.fail("init driver fails ", exception);
        }
    }

    public SeleniumWebDriverManager(Long threadId, Class<? extends WebDriver> driverInstance, Duration duration) {
        if (this.isDriverMatchTo(driverInstance, ChromeDriver.class)) {
            this.driver.set(WebDriverManager.chromedriver().create());
            this.webDriverWait.set(new WebDriverWait(this.driver.get(), duration));
            this.webDriverDecorator.put(threadId, new EventFiringDecorator<>(new DriverEventListener()));
            this.getDriverDecorator().decorate(this.driver.get());
        } else if (this.isDriverMatchTo(driverInstance, FirefoxDriver.class)) {
            this.driver.set(WebDriverManager.firefoxdriver().create());
            this.webDriverWait.set(new WebDriverWait(this.driver.get(), duration));
            this.webDriverDecorator.put(threadId, new EventFiringDecorator<>(new DriverEventListener()));
            this.getDriverDecorator().decorate(this.driver.get());
        } else Assertions.fail("init driver fails supply driver instance");
    }

    private synchronized WebDriverManager manager(Class<? extends WebDriver> driverInstance) {
        WebDriverManager instance = WebDriverManager.getInstance(driverInstance);
        instance.setup();
        return instance;
    }

    private synchronized <T> boolean isDriverMatchTo(Class<? extends WebDriver> driverInstance, Class<T> tClass) {
        try {
            return driverInstance.getName().equalsIgnoreCase(tClass.getName());
        } catch (Exception exception) {
            return false;
        }
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
    public void click(List<ExpectedCondition<WebElement>> expectedConditions) {
        AtomicBoolean find = new AtomicBoolean(false);
        AtomicReference<Exception> errors = new AtomicReference<>();
        expectedConditions.stream().parallel().forEach(condition -> {
            try {
                this.getWebDriverWait()
                        .withTimeout(this.generalTimeOut)
                        .pollingEvery(this.pollingEvery)
                        .until(condition)
                        .click();
                find.set(true);
            } catch (Exception exception) {
                errors.set(exception);
            }
        });
        if (!find.get()) Assertions.fail("Error click on element", errors.get());
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
