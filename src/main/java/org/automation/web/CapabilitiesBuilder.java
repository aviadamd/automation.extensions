package org.automation.web;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.internal.Require;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class CapabilitiesBuilder {
    private CapabilitiesBuilder() {}
    public static CapabilitiesBuilder getInstance() { return LazyHolder.INSTANCE; }
    public DesiredCapabilities buildCapabilities() { return this.capabilities; }
    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    private AbstractDriverOptions<?> driverOptions = null;
    public AbstractDriverOptions<?> buildOptions() { return driverOptions; }
    private static class LazyHolder {
        private static final CapabilitiesBuilder INSTANCE = new CapabilitiesBuilder();
    }

    public CapabilitiesBuilder merge(DesiredCapabilities capabilities) {
        this.capabilities.merge(capabilities);
        return this;
    }
    public CapabilitiesBuilder addProxy(Proxy proxy) {
        this.capabilities.setCapability("proxy", Require.nonNull("Proxy", proxy));
        return this;
    }

    public CapabilitiesBuilder addAcceptInsecureCerts(boolean acceptInsecureCerts) {
        this.capabilities.setCapability("acceptInsecureCerts", acceptInsecureCerts);
        return this;
    }

    public void addOptions(ChromeOptions chromeOptions) {
        this.capabilities.merge(new ChromeOptions().merge(chromeOptions));
        this.driverOptions = chromeOptions;
        Assertions.assertNotNull(this.driverOptions);
    }

    public void addOptions(FirefoxOptions firefoxOptions) {
        this.capabilities.merge(new FirefoxOptions().merge(firefoxOptions));
        this.driverOptions = firefoxOptions;
        Assertions.assertNotNull(this.driverOptions);
    }

    public void addOptions(SafariOptions safariOptions) {
        this.capabilities.merge(new SafariOptions().merge(safariOptions));
        this.driverOptions = safariOptions;
        Assertions.assertNotNull(this.driverOptions);
    }

    public void addOptions(WebDriver driver, AbstractDriverOptions<?> options) {
        if (driver.getClass().getName().equalsIgnoreCase(ChromeDriver.class.getName())) {
            this.capabilities.merge(new ChromeOptions().merge(options));
        } else if (driver.getClass().getName().equalsIgnoreCase(FirefoxDriver.class.getName())) {
            this.capabilities.merge(new FirefoxOptions().merge(options));
        }

        this.driverOptions = options;
        Assertions.assertNotNull(this.driverOptions);
    }

    public CapabilitiesBuilder setBrowserVersion(String browserVersion) {
        this.capabilities.setCapability("browserVersion", Require.nonNull("Browser version", browserVersion));
        return this;
    }

    public CapabilitiesBuilder setPlatformName(String platformName) {
        this.capabilities.setCapability("platformName", Require.nonNull("Platform Name", platformName));
        return this;
    }

    public Set<String> getCapabilitiesList() {
        TreeSet<String> names = new TreeSet<>(this.capabilities.getCapabilityNames());
        return Collections.unmodifiableSet(names);
    }

}
