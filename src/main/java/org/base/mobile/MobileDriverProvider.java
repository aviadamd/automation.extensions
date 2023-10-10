package org.base.mobile;

import io.appium.java_client.*;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import io.appium.java_client.android.appmanagement.AndroidRemoveApplicationOptions;
import io.appium.java_client.android.appmanagement.AndroidTerminateApplicationOptions;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.driverscripts.ScriptOptions;
import io.appium.java_client.driverscripts.ScriptValue;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.serverevents.CustomEvent;
import io.appium.java_client.serverevents.ServerEvents;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.base.anontations.WebElementGestures;
import org.base.anontations.MobileGestures;
import org.base.mobile.data.ElementsAttributes;
import org.base.mobile.gestures.MobileSwipeExtensions;
import org.extensions.web.WebDriverListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.net.URL;
import java.time.Duration;
import java.util.*;

@Slf4j
@Augmentable
public class MobileDriverProvider implements
        WebElementGestures, MobileGestures,
        ExecutesMethod, ExecutesDriverScript,
        LogsEvents, HasBrowserCheck, HasSettings  {

    private Duration generalTimeOut = Duration.ofSeconds(15);
    private Duration pollingEvery = Duration.ofSeconds(5);
    private final ThreadLocal<IOSDriver> iosDriver = new ThreadLocal<>();
    private final ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<>();
    private final ThreadLocal<AppiumWebDriverWaitExtensions> appiumWebDriverWait = new ThreadLocal<>();
    private final ThreadLocal<MobileSwipeExtensions> mobileSwipeExtensions = new ThreadLocal<>();
    private final ThreadLocal<AppLauncherExtensions> appLauncherExtensions = new ThreadLocal<>();
    private final ThreadLocal<MobileDriverType> mobileDriverType = new ThreadLocal<>();


    public IOSDriver getIosDriver() { return this.iosDriver.get(); }
    public AndroidDriver getAndroidDriver() { return this.androidDriver.get(); }
    public AppiumWebDriverWaitExtensions getWebDriverWait() { return this.appiumWebDriverWait.get(); }
    public MobileSwipeExtensions getSwipeExtensions() { return this.mobileSwipeExtensions.get(); }
    public AppLauncherExtensions getAppLauncherExtensions() { return this.appLauncherExtensions.get(); }
    public MobileDriverType getDriverType() { return this.mobileDriverType.get(); }

    public boolean isAndroid() {
        return this.mobileDriverType.get()
                .getDriverType()
                .getDriverName()
                .equalsIgnoreCase(DriverType.ANDROID.getDriverName());
    }

    public boolean isIos() {
        return this.mobileDriverType.get()
                .getDriverType()
                .getDriverName()
                .equalsIgnoreCase(DriverType.IOS.getDriverName());
    }

    public WebDriver getMobileDriver() {
        return switch (this.getDriverType().getDriverType()) {
            case ANDROID -> this.getAndroidDriver();
            case IOS -> this.getIosDriver();
            default -> throw new RuntimeException("unable to identify driver instance");
        };
    }

    public AppiumDriver getAppiumDriver() {
        return  (AppiumDriver) this.getMobileDriver();
    }

    public MobileDriverProvider oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    /**
     *
     * @param driverType
     * @param uiAutomator2Options
     * @param xcuiTestOptions
     * @param driverPath
     */
    public MobileDriverProvider(
            DriverType driverType, UiAutomator2Options uiAutomator2Options,
            XCUITestOptions xcuiTestOptions, String driverPath, Duration implicitlyWait) {
        try {
            this.mobileDriverType.set(new MobileDriverType(driverType));
            switch (this.mobileDriverType.get().getDriverType()) {
                case ANDROID -> {
                    EventFiringDecorator<AndroidDriver> decorator = new EventFiringDecorator<>(new WebDriverListenerImpl());
                    this.androidDriver.set(decorator.decorate(new AndroidDriver(new URL(driverPath), uiAutomator2Options)));
                    this.appiumWebDriverWait.set(new AppiumWebDriverWaitExtensions(this.androidDriver.get()));
                    this.androidDriver.get().manage().timeouts().implicitlyWait(implicitlyWait);
                }
                case IOS -> {
                    EventFiringDecorator<IOSDriver> decorator = new EventFiringDecorator<>(new WebDriverListenerImpl());
                    this.iosDriver.set(decorator.decorate(new IOSDriver(new URL(driverPath), xcuiTestOptions)));
                    this.appiumWebDriverWait.set(new AppiumWebDriverWaitExtensions(this.iosDriver.get()));
                    this.iosDriver.get().manage().timeouts().implicitlyWait(implicitlyWait);
                }
                case UNKNOWN -> throw new RuntimeException("driver type is not android and not ios");
            }

            this.mobileSwipeExtensions.set(new MobileSwipeExtensions(this));
            this.appLauncherExtensions.set(new AppLauncherExtensions(this));
        } catch (Exception exception) {
            throw new RuntimeException("init driver fail " + exception.getMessage(), exception);
        }
    }

    public MobileDriverProvider(DriverType driverType, DesiredCapabilities caps, String driverPath, int implicitlyWait) {
        try {
            this.mobileDriverType.set(new MobileDriverType(driverType));
            switch (this.mobileDriverType.get().getDriverType()) {
                case ANDROID -> {
                    this.androidDriver.set(new AndroidDriver(new URL(driverPath), caps));
                    this.appiumWebDriverWait.set(new AppiumWebDriverWaitExtensions(this.androidDriver.get()));
                    this.androidDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWait));
                }
                case IOS -> {
                    this.iosDriver.set(new IOSDriver(new URL(driverPath), caps));
                    this.appiumWebDriverWait.set(new AppiumWebDriverWaitExtensions(this.iosDriver.get()));
                    this.iosDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWait));
                }
                case UNKNOWN -> throw new RuntimeException("driver type is not android and not ios");
            }

            this.mobileSwipeExtensions.set(new MobileSwipeExtensions(this));
            this.appLauncherExtensions.set(new AppLauncherExtensions(this));
        } catch (Exception exception) {
            throw new RuntimeException("init driver fail " + exception.getMessage(), exception);
        }
    }

    /**
     *
     * @param connectionState
     * @return
     */
    public ConnectionState setAndroidDeviceConnectionState(ConnectionStateBuilder connectionState) {
        return this.androidDriver.get().setConnection(connectionState.build());
    }

    /**
     * setAndroidInstallOptions
     *   @param appPath
     *   @param options
     *  new AndroidInstallApplicationOptions()
     *  .withAllowTestPackagesDisabled()
     *  .withGrantPermissionsDisabled()
     *  .withTimeout(Duration.of(100, ChronoUnit.SECONDS))
     *  .withAllowTestPackagesEnabled()
     *  .withUseSdcardEnabled();
     */
    public void setAndroidInstallOptions(String appPath, AndroidInstallApplicationOptions options) {
        this.androidDriver.get().installApp(appPath, options);
    }

    public void setAndroidRemoveOptions(String appPath, AndroidRemoveApplicationOptions options) {
        this.androidDriver.get().removeApp(appPath, options);
    }

    public void setAndroidTerminateAppOptions(String appPath, AndroidTerminateApplicationOptions options) {
        this.androidDriver.get().terminateApp(appPath, options);
    }

    /**
     * setAndroidActivity
     * @param appPackage ...
     * @param appActivity ...
     * activity.isStopApp();
     * activity.setAppWaitPackage("")
     * .setAppWaitPackage("")
     * .setIntentAction("")
     * .setIntentFlags("")
     *  .setOptionalIntentArguments("");
     * @return setAndroidActivity options
     */
    public Activity getAndroidActivity(String appPackage, String appActivity) {
        try {
            return new Activity(appPackage, appActivity);
        } catch (Exception exception) {
            throw new RuntimeException("set android activity error", exception);
        }
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
        this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
    }

    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition) {
        this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .click();
    }
    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition, Duration generalTimeOut, Duration pollingEvery) {
        this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(generalTimeOut)
                .pollingEvery(pollingEvery)
                .until(expectedCondition)
                .click();
    }

    @Override
    public void sendKeys(ExpectedCondition<WebElement> expectedCondition, CharSequence... keysToSend) {
        this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .sendKeys(keysToSend);
    }

    @Override
    public void sendKeys(WebElement element, CharSequence... keysToSend) {
        this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(keysToSend);
    }

    @Override
    public String getAttribute(WebElement element, String name) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getAttribute(name));
    }
    @Override
    public String getAttribute(
            WebElement element,
            Pair<ElementsAttributes.AndroidElementsAttributes,
                    ElementsAttributes.IosElementsAttributes> attributesPair) {
        String setAttribute = isAndroid() ? attributesPair.getLeft().getTag() : attributesPair.getRight().getTag();
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getAttribute(setAttribute));
    }

    @Override
    public String getText(WebElement element) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getText());
    }

    @Override
    public List<WebElement> findElements(By by) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElements(by));
    }

    public WebElement findElement(WebElement webElement, By child) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> webElement.findElement(child));
    }

    @Override
    public WebElement findElement(By by) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(by));
    }

    @Override
    public WebElement findElement(ExpectedCondition<WebElement> expectedConditions) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedConditions);
    }

    @Override
    public List<WebElement> findElements(By byFather, By son) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(byFather).findElements(son));
    }

    @Override
    public WebElement findElement(By byFather, By son) {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(byFather).findElement(son));
    }

    @Override
    public String getPageSource() {
        return this.getWebDriverWait()
                .getWebDriverWait()
                .withTimeout(Duration.ofSeconds(2))
                .pollingEvery(Duration.ofSeconds(1))
                .until(WebDriver::getPageSource);
    }

    @Override
    public void get(String url) {
        this.getMobileDriver().get(url);
    }

    @Override
    public void close() {
        if (this.isAndroid()) {
            this.getAndroidDriver().closeApp();
        } else this.getIosDriver().closeApp();
    }

    @Override
    public void quit() {
        if (this.isAndroid()) {
            this.getAndroidDriver().closeApp();
        } else this.getIosDriver().closeApp();
    }


}
