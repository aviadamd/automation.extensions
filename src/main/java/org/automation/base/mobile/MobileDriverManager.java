package org.automation.base.mobile;

import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.driverscripts.ScriptOptions;
import io.appium.java_client.driverscripts.ScriptValue;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.serverevents.CustomEvent;
import io.appium.java_client.serverevents.ServerEvents;
import lombok.extern.slf4j.Slf4j;
import org.automation.base.WebElementGestures;
import org.extensions.mobile.CapsReader;
import org.extensions.DriverEventListener;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import static java.lang.System.getProperty;

@Slf4j
public class MobileDriverManager implements
        WebElementGestures, ExecutesMethod,
        ExecutesDriverScript, LogsEvents,
        HasBrowserCheck, HasSettings  {
    private Duration generalTimeOut = Duration.ofSeconds(5);
    private Duration pollingEvery = Duration.ofSeconds(1);
    private final ThreadLocal<IOSDriver> iosDriver = new ThreadLocal<>();
    private final ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<>();
    private final ThreadLocal<AppiumFluentWait<WebDriver>> webDriverWait = new ThreadLocal<>();
    public ThreadLocal<AppiumFluentWait<WebDriver>> getWebDriverWait() { return webDriverWait; }

    public WebDriver getMobileDriver() {
        return Objects.equals(getProperty("client"), "android") ? this.androidDriver.get() : this.iosDriver.get();
    }
    public MobileDriverManager oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    public MobileDriverManager(UiAutomator2Options options, String url) {
        try {
            this.androidDriver.set(new AndroidDriver(new URL(url), options));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    public MobileDriverManager(String type, DesiredCapabilities caps, String url) {
        try {
            switch (type) {
                case "ios":
                    this.iosDriver.set(new IOSDriver(new URL(url), caps));
                    this.iosDecorator(this.iosDriver.get());
                    this.webDriverWait.set(new AppiumFluentWait<>(this.getMobileDriver()));
                    break;
                case "android":
                    this.androidDriver.set(new AndroidDriver(new URL(url), caps));
                    this.androidDecorator(this.androidDriver.get());
                    this.webDriverWait.set(new AppiumFluentWait<>(this.getMobileDriver()));
                    break;
            }
        } catch (Exception exception) {
            Assertions.fail("init driver fail ", exception);
        }
    }
    public MobileDriverManager(CapsReader capsReader) {
        this(capsReader.getJsonObject().getClient(), capsReader.getCapabilities(), capsReader.getJsonObject().getAppiumUrl());
    }
    private void iosDecorator(IOSDriver iosDriver) {
         ThreadLocal<EventFiringDecorator<IOSDriver>> iosDecorator = new ThreadLocal<>();
         iosDecorator.set(new EventFiringDecorator<>(new DriverEventListener()));
         iosDecorator.get().decorate(iosDriver);
    }
    private void androidDecorator(AndroidDriver androidDriver) {
        ThreadLocal<EventFiringDecorator<AndroidDriver>> androidDecorator = new ThreadLocal<>();
        androidDecorator.set(new EventFiringDecorator<>(new DriverEventListener()));
        androidDecorator.get().decorate(androidDriver);
    }
    public void activate(String appBundle) {
        if (getProperty("client").equals("android")) {
            this.androidDriver.get().activateApp(appBundle);
        } else this.iosDriver.get().activateApp(appBundle);
    }
    @Override
    public Response execute(String driverCommand, Map<String, ?> map) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().execute(driverCommand, map);
        } else return this.androidDriver.get().execute(driverCommand, map);
    }

    @Override
    public Response execute(String driverCommand) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().execute(driverCommand);
        } else return this.androidDriver.get().execute(driverCommand);
    }

    @Override
    public Capabilities getCapabilities() {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().getCapabilities();
        } else return this.androidDriver.get().getCapabilities();
    }

    @Override
    public ScriptValue executeDriverScript(String script, @Nullable ScriptOptions options) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().executeDriverScript(script, options);
        } else return this.androidDriver.get().executeDriverScript(script, options);
    }

    @Override
    public ScriptValue executeDriverScript(String script) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().executeDriverScript(script);
        } else return this.androidDriver.get().executeDriverScript(script);
    }

    @Override
    public boolean isBrowser() {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().isBrowser();
        } else return this.androidDriver.get().isBrowser();
    }

    @Override
    public HasSettings setSetting(Setting setting, Object value) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().setSetting(setting, value);
        } else return this.androidDriver.get().setSetting(setting, value);
    }

    @Override
    public HasSettings setSetting(String settingName, Object value) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().setSetting(settingName, value);
        } else return this.androidDriver.get().setSetting(settingName, value);
    }

    @Override
    public HasSettings setSettings(EnumMap<Setting, Object> settings) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().setSettings(settings);
        } else return this.androidDriver.get().setSettings(settings);
    }
    @Override
    public HasSettings setSettings(Map<String, Object> settings) {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().setSettings(settings);
        } else return this.androidDriver.get().setSettings(settings);
    }

    @Override
    public Map<String, Object> getSettings() {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().getSettings();
        } else return this.androidDriver.get().getSettings();
    }

    @Override
    public void logEvent(CustomEvent event) {
        if (getProperty("client").equals("android")) {
            this.iosDriver.get().logEvent(event);
        } else this.androidDriver.get().logEvent(event);
    }

    @Override
    public ServerEvents getEvents() {
        if (getProperty("client").equals("android")) {
            return this.iosDriver.get().getEvents();
        } else return this.androidDriver.get().getEvents();
    }
    @Override
    public void click(WebElement element) {
        element.click();
    }
    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition) {
        this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
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
                        .get()
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

    public void sendKeys(ExpectedCondition<WebElement> expectedCondition, CharSequence... keysToSend) {
        this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .sendKeys(keysToSend);
    }

    @Override
    public void sendKeys(WebElement element, CharSequence... keysToSend) {
        this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(keysToSend);
    }
    @Override
    public String getAttribute(WebElement element, String name) {
        return this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getAttribute(name));
    }

    @Override
    public String getText(WebElement element) {
        return this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getText());
    }

    public List<WebElement> findElements(By by) {
        return this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElements(by));
    }
    public WebElement findElement(By by) {
        return this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(by));
    }
    public String getPageSource() {
        return this.getWebDriverWait()
                .get()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(WebDriver::getPageSource);
    }
}
