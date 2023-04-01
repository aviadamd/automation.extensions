package org.extensions.automation.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.base.configuration.PropertiesManager;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.extensions.anontations.ProviderConfiguration;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.factory.JunitAnnotationHandler;
import org.data.files.jsonReader.FilesHelper;
import org.data.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.utils.mongo.morphia.MorphiaRepository;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.time.Duration;
import java.util.Optional;

public class WebSharedObjectsProviderExtension implements ParameterResolver,
        BeforeAllCallback, BeforeEachCallback, AfterEachCallback,
        AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final ThreadLocal<WebSharedObjects> webSharedObjects = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext extension) {
        return parameter.getParameter().getType() == WebSharedObjects.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context)  {
        if (context.getElement().isPresent()) {
            try {
                return this.webSharedObjects.get();
            } catch (Exception exception) {
                throw new RuntimeException("fail resolve web shared objects provider initiation", exception);
            }
        } throw new RuntimeException("fail resolve web shared objects provider initiation");
    }

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<ProviderConfiguration> providerConfiguration = this.readAnnotation(context, ProviderConfiguration.class);
            if (providerConfiguration.isPresent()) {
                this.webSharedObjects.set(new WebSharedObjects());
                this.webSharedObjects.get().setWebConfiguration(new PropertiesManager().getOrCreate(WebConfiguration.class));
                this.initDriver(providerConfiguration.get(), this.initProxy());
                this.initJackson(providerConfiguration.get());
                this.initMongo(providerConfiguration.get());
            } else throw new RuntimeException("ProviderConfiguration annotation is missing in test method");
        } else throw new RuntimeException("fail provider initiation");
    }
    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                if (this.webSharedObjects.get().getMobProxyExtension() != null && this.webSharedObjects.get().getMobProxyExtension().getServer().getHar() != null) {
                    String testPath = System.getProperty("user.dir") + "/target/harFiles";
                    String testName = context.getRequiredTestMethod().getName();
                    FilesHelper filesHelper = new FilesHelper();
                    filesHelper.createDirectory(testPath);
                    File file = new File(testPath + "/" + testName + ".json");
                    this.webSharedObjects.get().getMobProxyExtension().writeHarFile(file, this.webSharedObjects.get().getMobProxyExtension().getServer().getHar().getLog());
                }
                this.webSharedObjects.get().getDriverManager().quit();
            } catch (Exception exception) {
                Assertions.fail("afterEach error ", exception);
            }
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                if (this.webSharedObjects.get().getMobProxyExtension().getServer() != null) {
                    this.webSharedObjects.get().getMobProxyExtension().getServer().stop();
                }
            } catch (Exception exception) {
                Assertions.fail("close proxy error ", exception);
            }
        }
    }

    private synchronized void initDriver(ProviderConfiguration providerConfiguration, DesiredCapabilities capabilities) {
        try {
            Duration duration = Duration.ofSeconds(providerConfiguration.driverProvider().generalTo());
            String url = this.webSharedObjects.get().getWebConfiguration().projectUrl();
            String client = this.webSharedObjects.get().getWebConfiguration().projectClient();
            this.webSharedObjects.get().setDriverManager(new SeleniumWebDriverProvider(url, duration, this.setWebDriver(client, capabilities)));
        } catch (Exception exception) {
            Assertions.fail("initDriver error " + exception, exception);
        }
    }

    private synchronized void initJackson(ProviderConfiguration providerConfiguration) {
        try {
            String path = System.getProperty("user.dir") + "/" + providerConfiguration.jacksonProvider().dir();
            File jsonPath = new File(path + "/" + providerConfiguration.jacksonProvider().fileName());
            this.webSharedObjects.get().setJacksonExtension(new JacksonExtension<>(path, jsonPath, providerConfiguration.jacksonProvider().classObject()));
        } catch (Exception exception) {
            Assertions.fail("initJackson error " + exception, exception);
        }
    }

    private synchronized void initMongo(ProviderConfiguration providerConfiguration) {
        try {
            MorphiaRepository repository = new MorphiaRepository(providerConfiguration.dbProvider().host(), providerConfiguration.dbProvider().dbName());
            this.webSharedObjects.get().setMorphiaRepository(repository);
        } catch (Exception exception) {
            Assertions.fail("initMongo error " + exception, exception);
        }
    }

    private synchronized DesiredCapabilities initProxy() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        try {
            this.webSharedObjects.get().setMobProxyExtension(new MobProxyExtension(MobProxyExtension.ProxyType.WEB, Inet4Address.getLocalHost()));
            capabilities.setCapability(CapabilityType.PROXY, this.webSharedObjects.get().getMobProxyExtension().getProxy());
            capabilities.acceptInsecureCerts();
            this.webSharedObjects.get().getMobProxyExtension().getServer().newHar();
        } catch (Exception exception) {
            Assertions.fail("initProxy error " + exception, exception);
        }
        return capabilities;
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

    private WebDriver setWebDriver(String client, DesiredCapabilities capabilities) {
        switch (client) {
            case "chrome" -> { return this.initChromeDriver(new WebDriverOptions(), capabilities); }
            case "firefox" -> { return this.initFireFoxDriver(new WebDriverOptions(), capabilities); }
            default -> throw new RuntimeException("you most provide chrome or firefox driver name");
        }
    }

    /**
     * String url = "http://chromedriver.chromium.org/downloads"
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
     * "https://github.com/mozilla/geckodriver/releases"
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
