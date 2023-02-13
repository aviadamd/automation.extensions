package org.automation.mobile;

import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.driverscripts.ScriptOptions;
import io.appium.java_client.driverscripts.ScriptValue;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.serverevents.CustomEvent;
import io.appium.java_client.serverevents.ServerEvents;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.slf4j.Slf4j;
import org.automation.WebElementGestures;
import org.extensions.automation.DriverEventListener;
import org.extensions.automation.mobile.CapsReaderAdapter;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.data.StringsUtilities;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Augmentable
public class MobileDriverManager implements
        WebElementGestures, ExecutesMethod,
        ExecutesDriverScript, LogsEvents,
        HasBrowserCheck, HasSettings  {
    private Duration generalTimeOut = Duration.ofSeconds(15);
    private Duration pollingEvery = Duration.ofSeconds(5);
    private final ThreadLocal<IOSDriver> iosDriver = new ThreadLocal<>();
    private final ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<>();
    private final ThreadLocal<AppiumFluentWait<WebDriver>> webDriverWait = new ThreadLocal<>();
    private final ThreadLocal<EventFiringDecorator<IOSDriver>> iosDecorator = new ThreadLocal<>();
    private final ThreadLocal<EventFiringDecorator<AndroidDriver>> androidDecorator = new ThreadLocal<>();
    public IOSDriver getIosDriver() { return this.iosDriver.get(); }
    public AndroidDriver getAndroidDriver() { return this.androidDriver.get(); }
    public AppiumFluentWait<WebDriver> getWebDriverWait() { return this.webDriverWait.get(); }

    private boolean isAndroid() {
        return System.getProperty("project.mobile.client").equalsIgnoreCase("android");
    }

    public static boolean isAndroidClient() {
        return System.getProperty("project.mobile.client").equalsIgnoreCase("android");
    }

    public WebDriver getMobileDriver() {
        return this.isAndroid() ? this.getAndroidDriver() : this.getIosDriver();
    }
    public MobileDriverManager oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    /**
     * @param type
     * @param caps
     * @param appiumBasePath appium url connection
     */
    public MobileDriverManager(String type, DesiredCapabilities caps, String appiumBasePath) {
        try {
            switch (type) {
                case "IOS" -> {
                    this.iosDriver.set(new IOSDriver(new URL(appiumBasePath), caps));
                    this.webDriverWait.set(new AppiumFluentWait<>(this.getMobileDriver()));
                }
                case "ANDROID" -> {
                    this.androidDriver.set(new AndroidDriver(new URL(appiumBasePath), caps));
                    this.webDriverWait.set(new AppiumFluentWait<>(this.getMobileDriver()));
                }
            }
        } catch (Exception exception) {
            Assertions.fail("init driver fail ", exception);
        }
    }

    /**
     * MobileDriverManager
     * @param capsReader
     */
    public MobileDriverManager(CapsReaderAdapter capsReader) {
        this(capsReader.getJsonObject().getClient(), capsReader.getCapabilities(), capsReader.getJsonObject().getAppiumBasePath());
    }

    private synchronized void appiumLocal(String appiumBasePath) {
        String ip = StringsUtilities.splitString(appiumBasePath,"//",1);
        ip = StringsUtilities.splitString(ip,":",0);
        String port = StringsUtilities.splitString(appiumBasePath,":",2);
        port = StringsUtilities.splitString(port,"/",0);
        AtomicReference<AppiumDriverLocalService> appiumLocalService = new AtomicReference<>();
        appiumLocalService.set(new AppiumServiceBuilder()
                .usingDriverExecutable(new File(System.getProperty("project.node.js")))
                .withAppiumJS(new File(System.getProperty("project.appium.exe")))
                .withIPAddress(ip)
                .usingPort(Integer.parseInt(port))
                .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub")
                .withArgument(GeneralServerFlag.ALLOW_INSECURE)
                .withArgument(GeneralServerFlag.ASYNC_TRACE)
                .build());
        appiumLocalService.get().start();
    }

    private synchronized DesiredCapabilities capsForChromeAndroid(DesiredCapabilities caps, String browserVersion, String chromedriverPath) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("chromedriverExecutable", chromedriverPath);
        capabilities.merge(caps);
        return capabilities;
    }

    public void activate(String appBundle) {
        if (this.isAndroid()) {
            this.getAndroidDriver().activateApp(appBundle);
        } else this.getIosDriver().activateApp(appBundle);
    }
    @Override
    public Response execute(String driverCommand, Map<String, ?> map) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().execute(driverCommand, map);
        } else return this.getIosDriver().execute(driverCommand, map);
    }

    @Override
    public Response execute(String driverCommand) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().execute(driverCommand);
        } else return this.getIosDriver().execute(driverCommand);
    }

    @Override
    public Capabilities getCapabilities() {
        if (this.isAndroid()) {
            return this.getAndroidDriver().getCapabilities();
        } else return this.getIosDriver().getCapabilities();
    }

    @Override
    public ScriptValue executeDriverScript(String script, @Nullable ScriptOptions options) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().executeDriverScript(script, options);
        } else return this.getIosDriver().executeDriverScript(script, options);
    }

    @Override
    public ScriptValue executeDriverScript(String script) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().executeDriverScript(script);
        } else return this.getIosDriver().executeDriverScript(script);
    }

    @Override
    public boolean isBrowser() {
        if (this.isAndroid()) {
            return this.getAndroidDriver().isBrowser();
        } else return this.getIosDriver().isBrowser();
    }

    @Override
    public HasSettings setSetting(Setting setting, Object value) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().setSetting(setting, value);
        } else return this.getIosDriver().setSetting(setting, value);
    }

    @Override
    public HasSettings setSetting(String settingName, Object value) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().setSetting(settingName, value);
        } else return this.getIosDriver().setSetting(settingName, value);
    }

    @Override
    public HasSettings setSettings(EnumMap<Setting, Object> settings) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().setSettings(settings);
        } else return this.getIosDriver().setSettings(settings);
    }
    @Override
    public HasSettings setSettings(Map<String, Object> settings) {
        if (this.isAndroid()) {
            return this.getAndroidDriver().setSettings(settings);
        } else return this.getIosDriver().setSettings(settings);
    }

    @Override
    public Map<String, Object> getSettings() {
        if (this.isAndroid()) {
            return this.getAndroidDriver().getSettings();
        } else return this.getIosDriver().getSettings();
    }

    @Override
    public void logEvent(CustomEvent event) {
        if (this.isAndroid()) {
            this.getAndroidDriver().logEvent(event);
        } else this.getIosDriver().logEvent(event);
    }

    @Override
    public ServerEvents getEvents() {
        if (this.isAndroid()) {
            return this.getAndroidDriver().getEvents();
        } else return this.getIosDriver().getEvents();
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
    public void sendKeys(ExpectedCondition<WebElement> expectedCondition, CharSequence... keysToSend) {
        this.getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .sendKeys(keysToSend);
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
