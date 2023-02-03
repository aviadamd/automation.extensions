package org.extensions.automation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.automation.AutomationProperties;
import org.automation.web.HarObject;
import org.automation.web.SeleniumWebDriverManager;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.time.Duration;
import java.util.*;

@Slf4j
public class WebDriverProviderExtension implements ParameterResolver, AfterEachCallback, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<Proxy> seleniumProxy = new ThreadLocal<>();
    private final ThreadLocal<BrowserMobProxy> mobProxy = new ThreadLocal<>();
    private final ThreadLocal<SeleniumWebDriverManager> driverManager = new ThreadLocal<>();
    private final ThreadLocal<WebDriverManager> webDriverManager = new ThreadLocal<>();
    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == SeleniumWebDriverManager.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
            if (driverType.isPresent()) {

                this.mobProxy.set(this.setMobProxyServer(driverType.get().proxyPort()));
                this.seleniumProxy.set(this.setSeleniumProxy(this.mobProxy.get()));

                for (Class<? extends WebDriver> instance : driverType.get().driversInstance()) {
                    if (instance.getName().equalsIgnoreCase(ChromeDriver.class.getName())) {
                        this.driverManager.set(new SeleniumWebDriverManager(
                                AutomationProperties.getInstance().getProperty(driverType.get().baseUrl()),
                                Duration.ofSeconds(driverType.get().generalTo()),
                                WebDriverManager.getInstance(instance).capabilities(this.chromeOptions(this.seleniumProxy.get())))
                        );
                    } else if (instance.getName().equalsIgnoreCase(FirefoxDriver.class.getName())) {
                        this.driverManager.set(new SeleniumWebDriverManager(
                                AutomationProperties.getInstance().getProperty(driverType.get().baseUrl()),
                                Duration.ofSeconds(driverType.get().generalTo()),
                                WebDriverManager.getInstance(instance).capabilities(this.firefoxOptions(this.seleniumProxy.get())))
                        );
                    } else throw new RuntimeException(instance.getName() + " is not supported valid instance");
                }
            }
            return this.driverManager.get();
        } else throw new RuntimeException("WebDriverProviderExtension error");
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    if (this.mobProxy.get() != null && this.mobProxy.get().getHar() != null) {
                        String testName = context.getRequiredTestMethod().getName();
                        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
                        writer.writeValue(new File(System.getProperty("user.dir") + "/target/jsonFiles/" + testName + ".json"), new HarObject(testName, this.mobProxy.get().getHar().getLog().getEntries()));
                    }

//                    List<Map<String, Object>> logMessages = this.webDriverManager.get().getLogs();
//                    for (Map<String, Object> map : logMessages) {
//                        log.debug("[{}] [{}.{}] {}", map.get("datetime"), map.get("source").toString().toUpperCase(), String.format("%1$-7s", map.get("type").toString().toUpperCase()), map.get("message"));
//                    }
                }
            } catch (Exception exception) {
                Assertions.fail("generate har file error ",exception);
            }
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                   this.mobProxy.get().stop();
                }
            } catch (Exception exception) {
                Assertions.fail(exception);
            }
        }
    }

    public synchronized Proxy setSeleniumProxy(BrowserMobProxy browserMobProxy) {
        try {
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy);
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            seleniumProxy.setHttpProxy(hostIp + ":" + browserMobProxy.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + browserMobProxy.getPort());
            return seleniumProxy;
        } catch (Exception exception) {
            throw  new RuntimeException("init selenium proxy fails ", exception);
        }
    }
    public synchronized BrowserMobProxy setMobProxyServer(int port) {
        try {
            BrowserMobProxyServer mobProxyServer = new BrowserMobProxyServer();
            mobProxyServer.setTrustAllServers(true);
            mobProxyServer.setMitmDisabled(true);
            EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();
            captureTypes.addAll(CaptureType.getCookieCaptureTypes());
            captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
            captureTypes.addAll(CaptureType.getRequestCaptureTypes());
            captureTypes.addAll(CaptureType.getResponseCaptureTypes());
            mobProxyServer.enableHarCaptureTypes(captureTypes);
            mobProxyServer.newHar();
            mobProxyServer.start(port);
            return mobProxyServer;
        } catch (Exception exception) {
            throw new RuntimeException("init mob proxy fails ", exception);
        }
    }

    private synchronized ChromeOptions chromeOptions(Proxy proxy) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--dns-prefetch-disable");
        chromeOptions.addArguments("enable-automation");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-running-insecure-content");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.acceptInsecureCerts();
        return chromeOptions;
    }

    private FirefoxOptions firefoxOptions(Proxy proxy) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");
        firefoxOptions.addPreference("network.automatic-ntlm-auth.allow-non-fqdn", true);
        firefoxOptions.addPreference("network.negotiate-auth.delegation-uris", "http://,https://");
        firefoxOptions.addPreference("network.negotiate-auth.trusted-uris", "http://,https://");
        firefoxOptions.addPreference("network.http.phishy-userpass-length", 255);
        firefoxOptions.addPreference("security.csp.enable", false);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setAcceptUntrustedCertificates(true);
        firefoxOptions.merge(firefoxOptions);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.acceptInsecureCerts();
        return firefoxOptions;
    }

    @Override
    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                Assertions.fail("Fail read annotation from ExtentReportExtension", exception);
            }
        }
        return Optional.empty();
    }
}

