package org.extensions.automation.web;

import lombok.extern.slf4j.Slf4j;
import org.base.configuration.PropertiesManager;
import org.base.web.SeleniumWebDriverManager;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.factory.JunitAnnotationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
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
        ParameterResolver, BeforeEachCallback,
        BeforeAllCallback, AfterEachCallback,
        AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {

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
            if (driverType.isPresent()) {
                return this.driverManager.get();
            } else throw new RuntimeException("WebDriverType annotation is missing in test method");
    }
        throw new RuntimeException("WebDriverProviderExtension error");
    }

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
            if (driverType.isPresent()) {
                this.webProperties.set(new PropertiesManager().getOrCreate(WebConfiguration.class));
                this.initDriver(driverType.get());
            } else throw new RuntimeException("WebDriverType annotation is missing in test method");
        }
    }

    private synchronized void initDriver(WebDriverType driverType) {
        try {
            SeleniumWebDriverManager driverManager = new SeleniumWebDriverManager();
            Duration duration = driverManager.setWebDriverWaitDuration(driverType.durationOf(), driverType.generalTo());
            String url = this.webProperties.get().projectUrl();
            String projectClient = this.webProperties.get().projectClient();
            MobProxyExtension mobProxyExtension = new MobProxyExtension(MobProxyExtension.ProxyType.WEB, Inet4Address.getLocalHost());
            this.mobProxyExtension.set(mobProxyExtension);
            DesiredCapabilities capabilities = driverManager.initProxy(mobProxyExtension);
            this.driverManager.set(new SeleniumWebDriverProvider(url, duration, driverManager.setWebDriver(projectClient, capabilities)));
        } catch (Exception exception) {
            Assertions.fail("initDriver error " + exception, exception);
        }
    }
    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    if (this.mobProxyExtension.get().getServer() != null && this.mobProxyExtension.get().getServer().getHar() != null) {
                        String testPath = System.getProperty("user.dir") + "/target/har_files";
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

