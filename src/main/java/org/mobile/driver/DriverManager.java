package org.mobile.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import java.net.URL;

@Slf4j
public class DriverManager {
    private WebDriver driver = null;
    private IOSDriver iosDriver = null;
    private AndroidDriver androidDriver = null;
    public WebDriver getDriver() { return driver; }
    public AndroidDriver getAndroidDriver() { return this.androidDriver; }
    public IOSDriver getIosDriver() { return this.iosDriver; }
    public DriverManager(String type, String url) {
        try {
            switch (type) {
                case "ios":
                    this.iosDriver = this.iosDriver(new DriverEventListener(), url);
                    this.driver = this.iosDriver;
                    break;
                case "android":
                    this.androidDriver = this.androidDriver(new DriverEventListener(), url);
                    this.driver = this.androidDriver;
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private IOSDriver iosDriver(WebDriverListener driverListener, String url) {
        try {
            IOSDriver iosDriver = new IOSDriver(new URL(url), new MutableCapabilities()) {
                @Override
                protected void startSession(Capabilities capabilities) {
                    DesiredCapabilities serverCapabilities = new DesiredCapabilities();
                    serverCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                    serverCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
                    serverCapabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
                    serverCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
                    serverCapabilities.setCapability(MobileCapabilityType.APP, "");
                    capabilities.merge(serverCapabilities);
                }
            };
            return new EventFiringDecorator<>(IOSDriver.class, driverListener).decorate(iosDriver);
        } catch (Exception ignore) {
            return null;
        }
    }
    private AndroidDriver androidDriver(WebDriverListener driverListener, String url) {
        try {
            AndroidDriver androidDriver = new AndroidDriver(new URL(url), new MutableCapabilities()) {
                @Override
                protected void startSession(Capabilities capabilities) {
                    DesiredCapabilities serverCapabilities = new DesiredCapabilities();
                    serverCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                    serverCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
                    serverCapabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
                    serverCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
                    serverCapabilities.setCapability(MobileCapabilityType.APP, "");
                    capabilities.merge(serverCapabilities);
                }
            };
            return new EventFiringDecorator<>(AndroidDriver.class, driverListener).decorate(androidDriver);
        } catch (Exception ignore) {
            return null;
        }
    }
}
