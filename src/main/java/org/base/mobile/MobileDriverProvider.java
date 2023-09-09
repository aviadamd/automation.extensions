package org.base.mobile;

import io.appium.java_client.*;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.driverscripts.ScriptOptions;
import io.appium.java_client.driverscripts.ScriptValue;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.serverevents.CustomEvent;
import io.appium.java_client.serverevents.ServerEvents;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.base.anontations.WebElementGestures;
import org.base.configuration.MobileGestures;
import org.base.mobile.data.ElementsAttributes;
import org.extensions.automation.mobile.CapsReaderAdapter;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Response;
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
    private final ThreadLocal<MobileDriverType> mobileDriverType = new ThreadLocal<>();

    public IOSDriver getIosDriver() { return this.iosDriver.get(); }
    public AndroidDriver getAndroidDriver() { return this.androidDriver.get(); }
    public AppiumWebDriverWaitExtensions getWebDriverWaitExtension() { return this.appiumWebDriverWait.get(); }
    public MobileDriverType getDriverType() { return mobileDriverType.get(); }

    private boolean isAndroid() {
        switch (this.mobileDriverType.get().getDriverType()) {
            case ANDROID -> { return true; }
            case UNKNOWN, IOS -> { return false; }
            default -> Assertions.fail();
        }
        return false;
    }
    public WebDriver getMobileDriver() {
        return this.isAndroid() ? this.getAndroidDriver() : this.getIosDriver();
    }

    public MobileDriverProvider oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    /**
     * @param type
     * @param caps
     * @param appiumBasePath appium url connection
     */
    public MobileDriverProvider(String type, DesiredCapabilities caps, String appiumBasePath) {
        try {
            this.mobileDriverType.set(new MobileDriverType(type));
            switch (this.mobileDriverType.get().getDriverType()) {
                case ANDROID -> {
                    this.androidDriver.set(new AndroidDriver(new URL(appiumBasePath), caps));
                    this.appiumWebDriverWait.set(new AppiumWebDriverWaitExtensions(this));
                }
                case IOS -> {
                    this.iosDriver.set(new IOSDriver(new URL(appiumBasePath), caps));
                    this.appiumWebDriverWait.set(new AppiumWebDriverWaitExtensions(this));
                }
                case UNKNOWN -> throw new RuntimeException("driver type is not android and not ios");
            }
        } catch (Exception exception) {
            Assertions.fail("init driver fail " + exception.getMessage(), exception);
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
        this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
    }

    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition) {
        this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .click();
    }
    @Override
    public void click(ExpectedCondition<WebElement> expectedCondition, Duration generalTimeOut, Duration pollingEvery) {
        this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(generalTimeOut)
                .pollingEvery(pollingEvery)
                .until(expectedCondition)
                .click();
    }

    @Override
    public void sendKeys(ExpectedCondition<WebElement> expectedCondition, CharSequence... keysToSend) {
        this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(expectedCondition)
                .sendKeys(keysToSend);
    }

    @Override
    public void sendKeys(WebElement element, CharSequence... keysToSend) {
        this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(keysToSend);
    }

    @Override
    public String getAttribute(WebElement element, String name) {
        return this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getAttribute(name));
    }
    @Override
    public String getAttribute(WebElement element, Pair<ElementsAttributes.AndroidElementsAttributes, ElementsAttributes.IosElementsAttributes> attributesPair) {
        String setAttribute = isAndroid() ? attributesPair.getLeft().getTag() : attributesPair.getRight().getTag();
        return this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getAttribute(setAttribute));
    }

    @Override
    public String getText(WebElement element) {
        return this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> element.getText());
    }
    @Override
    public List<WebElement> findElements(By by) {
        return this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElements(by));
    }
    @Override
    public WebElement findElement(By by) {
        return this.getWebDriverWaitExtension()
                .getWebDriverWait()
                .withTimeout(this.generalTimeOut)
                .pollingEvery(this.pollingEvery)
                .until(condition -> condition.findElement(by));
    }

    @Override
    public String getPageSource() {
        return this.getWebDriverWaitExtension()
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
        if (this.getMobileDriver() != null) this.getMobileDriver().close();
    }

    @Override
    public void quit() {
        if (this.getMobileDriver() != null)  this.getMobileDriver().quit();
    }
}
