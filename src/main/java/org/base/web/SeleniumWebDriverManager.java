package org.base.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.automation.web.WebDriverOptions;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.time.Duration;

public class SeleniumWebDriverManager {

    public SeleniumWebDriverManager() {}

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

    public DesiredCapabilities setProxyCapabilities(MobProxyExtension mobProxyExtension) {
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
    public WebDriver initChromeDriver(WebDriverOptions options, DesiredCapabilities capabilities) {
        return this.chromeDriverManger()
                .capabilities(options.chromeOptions().merge(capabilities))
                .create();
    }

    /**
     * https://github.com/mozilla/geckodriver/releases
     * @param options
     * @param capabilities
     * @return
     */
    public WebDriver initFireFoxDriver(WebDriverOptions options, DesiredCapabilities capabilities) {
        return this.fireFoxDriverManager()
                .capabilities(options.firefoxOptions().merge(capabilities))
                .create();
    }

    public WebDriverManager fireFoxDriverManager() {
        WebDriverManager manager = WebDriverManager.firefoxdriver();
        manager.setup();
        return manager;
    }

    public WebDriverManager chromeDriverManger() {
        WebDriverManager manager = WebDriverManager.chromedriver();
        manager.setup();
        return manager;
    }
}
