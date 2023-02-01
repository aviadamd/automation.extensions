package org.automation.base.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.automation.base.WebElementGestures;
import org.extensions.automation.DriverEventListener;
import org.extensions.report.AspectExtension;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.Duration;
import java.util.*;
@Slf4j
@EnableAspectJAutoProxy
public class SeleniumWebDriverManager implements WebDriver, WebElementGestures {

    @Autowired
    public AspectExtension aspectExtension;
    private Duration generalTimeOut = Duration.ofSeconds(5);
    private Duration pollingEvery = Duration.ofSeconds(1);
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<WebDriverWait> webDriverWait = new ThreadLocal<>();
    private Map<Long, BrowserMobProxy> browserMobProxyServer;
    private void setBrowserMobProxyServer(Map<Long, BrowserMobProxy> browserMobProxyServer) { this.browserMobProxyServer = browserMobProxyServer; }
    public BrowserMobProxy getBrowserMobProxy() { return this.browserMobProxyServer.get(Thread.currentThread().getId()); }
    public WebDriver getDriver() { return driver.get(); }
    public WebDriverWait getWebDriverWait() { return this.webDriverWait.get(); }

    public SeleniumWebDriverManager oveRideTimeOut(Duration generalTimeOut, Duration pollingEvery) {
        this.generalTimeOut = generalTimeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    public SeleniumWebDriverManager() {}
    /**
     * WebDriverManagerExtension constructor
     * @param driverInstance pass as args
     * @param duration to web driver wait
     */
    public SeleniumWebDriverManager(Class<? extends WebDriver> driverInstance, Duration duration) {
        this.driver.set(WebDriverManager.getInstance(driverInstance).create());
        this.webDriverWait.set(new WebDriverWait(this.driver.get(), duration));
        this.webDriverWait.get().ignoring(NullPointerException.class);
        this.setDecorator(this.driver.get());
    }

    /**
     * WebDriverManagerExtension constructor
     * @param withProxy
     * @param baseUrl proxy url
     * @param driverInstance pass as args
     * @param duration to web driver wait
     */
    public SeleniumWebDriverManager(boolean withProxy, String baseUrl, Class<? extends WebDriver> driverInstance, Duration duration) {
        if (driverInstance.getName().equalsIgnoreCase(ChromeDriver.class.getName())) {
            if (withProxy) {
                this.driver.set(WebDriverManager.chromedriver()
                        .avoidBrowserDetection()
                        .capabilities(this.chromeOptions().merge(this.setProxyCapabilities(baseUrl)))
                        .create());
            } else this.driver.set(WebDriverManager.chromedriver().avoidBrowserDetection().create());
            if (!baseUrl.isEmpty()) this.driver.get().get(baseUrl);

        } else if (driverInstance.getName().equalsIgnoreCase(FirefoxDriver.class.getName())) {
            if (withProxy) {
                this.driver.set(WebDriverManager.firefoxdriver()
                        .avoidBrowserDetection()
                        .capabilities(this.firefoxOptions().merge(this.setProxyCapabilities(baseUrl)))
                        .create());
            } else this.driver.set(WebDriverManager.firefoxdriver().avoidBrowserDetection().create());
            if (!baseUrl.isEmpty()) this.driver.get().get(baseUrl);

        } else Assertions.fail("init driver fails supply driver instance");

        this.webDriverWait.set(new WebDriverWait(this.driver.get(), duration));
        this.setDecorator(this.driver.get());
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
    @Description("get title")
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
    public String getWindowHandle() {return this.getDriver().getWindowHandle();}
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
    @Description("click ")
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
    private DesiredCapabilities setProxyCapabilities(String url) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        try {

            this.setBrowserMobProxyServer(Map.of(Thread.currentThread().getId(), new BrowserMobProxyServer()));
            this.getBrowserMobProxy().setTrustAllServers(true);

            EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();
            captureTypes.addAll(CaptureType.getCookieCaptureTypes());
            captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
            captureTypes.addAll(CaptureType.getRequestCaptureTypes());
            captureTypes.addAll(CaptureType.getResponseCaptureTypes());

            this.getBrowserMobProxy().enableHarCaptureTypes(captureTypes);
            this.getBrowserMobProxy().newHar(System.getProperty("user.dir") + "/target/test.har");
            this.getBrowserMobProxy().start(0);

            Proxy proxy = ClientUtil.createSeleniumProxy(this.getBrowserMobProxy()).setHttpProxy(url).setSslProxy(url);
            desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

            return desiredCapabilities;

        } catch (Exception exception) {
            Assertions.fail("setProxy error ",exception);
        }
        return desiredCapabilities;
    }

    private ChromeOptions chromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--dns-prefetch-disable");
        chromeOptions.addArguments("enable-automation");
        chromeOptions.addArguments("disable-features=NetworkService");
        chromeOptions.setLogLevel(ChromeDriverLogLevel.INFO);
        return chromeOptions;
    }

    private FirefoxOptions firefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--disable-gpu");
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--disable-popup-blocking");
        firefoxOptions.addArguments("--disable-notifications");
        firefoxOptions.addArguments("--no-sandbox");
        firefoxOptions.addArguments("--dns-prefetch-disable");
        firefoxOptions.addArguments("enable-automation");
        firefoxOptions.addArguments("disable-features=NetworkService");
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.INFO);
        return firefoxOptions;
    }
    private void setDecorator(WebDriver driver) {
        EventFiringDecorator<WebDriver> webDriverDecorator = new EventFiringDecorator<>(new DriverEventListener());
        webDriverDecorator.decorate(driver);
    }

}
