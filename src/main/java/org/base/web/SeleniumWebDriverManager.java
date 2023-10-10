package org.base.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.extensions.web.WebDriverOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.time.Duration;

public class SeleniumWebDriverManager {

    public SeleniumWebDriverManager() {}

    private WebDriver driver;
    private Duration duration = Duration.ofSeconds(10);
    private DesiredCapabilities capabilities = new DesiredCapabilities();

    public SeleniumWebDriverManager setCapabilities(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public SeleniumWebDriverManager setWaitDuration(DurationOf durationOf, int to) {
        switch (durationOf) {
            case MINUTES -> this.duration = Duration.ofMinutes(to);
            case SECONDS -> this.duration = Duration.ofSeconds(to);
        }
        return this;
    }

    public SeleniumWebDriverManager build(String client) {
        WebDriverOptions driverOptions = new WebDriverOptions();

        try {

            final WebDriverManager instance = WebDriverManager.getInstance(client);

            switch (client) {
                case "chrome" -> instance.capabilities(driverOptions.chrome().merge(this.capabilities));
                case "firefox" -> instance.capabilities(driverOptions.firefox().merge(this.capabilities));
                default -> throw new RuntimeException("you most provide chrome or firefox driver name");
            }

            this.driver = instance
                    .timeout(20)
                    .avoidShutdownHook()
                    .avoidBrowserDetection()
                    .create();

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return this;
    }

    public Duration getDuration() {
        return duration;
    }

    public WebDriver getDriver() {
        return this.driver;
    }


}
