package org.extensions.automation.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.automation.configuration.PropertiesManager;
import org.automation.web.SeleniumWebDriverProvider;
import org.automation.web.WebConfiguration;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

@Slf4j
public class WebDriverProviderExtension implements
        ParameterResolver, BeforeEachCallback, AfterEachCallback,
        AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {

    private final ThreadLocal<DesiredCapabilities> capabilities = new ThreadLocal<>();
    private final ThreadLocal<WebConfiguration> webProperties = new ThreadLocal<>();
    private final ThreadLocal<SeleniumWebDriverProvider> driverManager = new ThreadLocal<>();
    private final ThreadLocal<MobProxyExtension> mobProxyExtension = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == SeleniumWebDriverProvider.class;
    }
    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
            if (driverType.isPresent()) return this.driverManager.get();
        }
        throw new RuntimeException("WebDriverProviderExtension error");
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {

                    this.webProperties.set(new PropertiesManager().getOrCreate(WebConfiguration.class));
                    this.mobProxyExtension.set(new MobProxyExtension(MobProxyExtension.ProxyType.WEB, driverType.get().proxyPort(), Inet4Address.getLocalHost()));
                    this.capabilities.set(new DesiredCapabilities());
                    this.capabilities.get().setCapability(CapabilityType.PROXY, this.mobProxyExtension.get().getProxy());
                    this.capabilities.get().acceptInsecureCerts();
                    this.mobProxyExtension.get().getServer().newHar();

                    if (this.webProperties.get().projectClient().equalsIgnoreCase("chrome")) {
                        this.driverManager.set(new SeleniumWebDriverProvider(
                                this.webProperties.get().projectUrl(),
                                Duration.ofSeconds(driverType.get().generalTo()),
                                WebDriverManager.chromedriver().capabilities(new WebDriverOptions().chromeOptions().merge(capabilities.get())).create()));
                    } else if (this.webProperties.get().projectClient().equalsIgnoreCase("firefox")) {
                        this.driverManager.set(new SeleniumWebDriverProvider(
                                this.webProperties.get().projectUrl(),
                                Duration.ofSeconds(driverType.get().generalTo()),
                                WebDriverManager.firefoxdriver().capabilities(new WebDriverOptions().firefoxOptions().merge(capabilities.get())).create()));
                    }
                }
            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
            }
        }
    }
    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    if (this.mobProxyExtension.get().getServer() != null && this.mobProxyExtension.get().getServer().getHar() != null) {
                        String testPath = System.getProperty("user.dir") + "/target/harFiles";
                        String testName = context.getRequiredTestMethod().getName();
                        Files.createDirectories(Path.of(testPath));
                        File file = new File(testPath + "/" + testName + ".json");
                        this.mobProxyExtension.get().writeHarFile(file, this.mobProxyExtension.get().getServer().getHar().getLog());
                    }
                    this.driverManager.get().getDriver().quit();
                }
            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
            }
        }
    }
    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent() && this.mobProxyExtension.get().getServer() != null) {
                    this.mobProxyExtension.get().getServer().stop();
                }
            } catch (Exception exception) {
                Assertions.fail("close proxy error ", exception);
            }
        }
    }
    @Override
    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                Assertions.fail("read annotation error ", exception);
            }
        }
        return Optional.empty();
    }
}

