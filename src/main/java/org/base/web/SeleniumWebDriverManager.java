package org.base.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.base.DurationOf;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.automation.web.WebDriverOptions;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.time.Duration;

public class SeleniumWebDriverManager {
    public Duration setWebDriverWaitDuration(DurationOf durationOf, int to) {
        Duration duration = Duration.ofSeconds(5);
        switch (durationOf) {
            case SECONDS -> duration = Duration.ofSeconds(to);
            case MINUTES -> duration = Duration.ofMinutes(to);
        }
        return duration;
    }

    public WebDriver setWebDriver(String client, DesiredCapabilities capabilities) {
        switch (client) {
            case "chrome" -> { return this.initChromeDriver(new WebDriverOptions(), capabilities); }
            case "firefox" -> { return this.initFireFoxDriver(new WebDriverOptions(), capabilities); }
            default -> throw new RuntimeException("you most provide chrome or firefox driver name");
        }
    }

    public DesiredCapabilities initProxy(MobProxyExtension mobProxyExtension) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        try {
            capabilities.setCapability(CapabilityType.PROXY, mobProxyExtension.getProxy());
            capabilities.acceptInsecureCerts();
            mobProxyExtension.getServer().newHar();
        } catch (Exception exception) {
            Assertions.fail("initProxy error " + exception, exception);
        }
        return capabilities;
    }
    /**
     * String url = http://chromedriver.chromium.org/downloads
     * @param options
     * @param capabilities
     * @return
     */
    private WebDriver initChromeDriver(WebDriverOptions options, DesiredCapabilities capabilities) {
        WebDriverManager manager = WebDriverManager.chromedriver();
        manager.setup();
        ChromeOptions chromeOptions = options.chromeOptions().merge(capabilities);
        return manager.capabilities(chromeOptions).create();
    }

    /**
     * https://github.com/mozilla/geckodriver/releases
     * @param options
     * @param capabilities
     * @return
     */
    private WebDriver initFireFoxDriver(WebDriverOptions options, DesiredCapabilities capabilities) {
        WebDriverManager manager = WebDriverManager.firefoxdriver();
        manager.setup();
        FirefoxOptions firefoxOptions = options.firefoxOptions().merge(capabilities);
        return manager.capabilities(firefoxOptions).create();
    }
}
