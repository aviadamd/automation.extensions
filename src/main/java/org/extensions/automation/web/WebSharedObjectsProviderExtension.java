package org.extensions.automation.web;

import org.automation.configuration.PropertiesManager;
import org.automation.web.SeleniumWebDriverManager;
import org.automation.web.WebConfiguration;
import org.extensions.anontations.ProviderConfiguration;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.factory.JunitAnnotationHandler;
import org.files.jsonReader.FilesHelper;
import org.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.mongo.morphia.MorphiaRepository;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Optional;
import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;
import static io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver;

public class WebSharedObjectsProviderExtension implements ParameterResolver,
        BeforeEachCallback, AfterEachCallback,
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
    public synchronized void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<ProviderConfiguration> testWith = this.readAnnotation(context, ProviderConfiguration.class);
                if (testWith.isPresent()) {
                    this.webSharedObjects.set(new WebSharedObjects());
                    this.webSharedObjects.get().setWebConfiguration(new PropertiesManager().getOrCreate(WebConfiguration.class));
                    this.initProvider(testWith.get());
                } else throw new RuntimeException("fail provider initiation");

            } catch (Exception exception) {
                throw new RuntimeException("fail provider initiation", exception);
            }
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
                this.webSharedObjects.get().getDriverManager().getDriver().quit();
            } catch (Exception exception) {
                Assertions.fail("generate har file error ", exception);
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
    private synchronized void initProvider(ProviderConfiguration testWith) {
        this.setDriver(testWith, this.setProxy(testWith));
        String path = System.getProperty("user.dir") + "/" + testWith.jacksonProvider().dir();
        this.webSharedObjects.get().setJacksonExtension(new JacksonExtension<>(path, new File(path + "/" + testWith.jacksonProvider().fileName()), testWith.jacksonProvider().classObject()));
        MorphiaRepository repository = new MorphiaRepository(testWith.dbProvider().host(), testWith.dbProvider().dbName());
        this.webSharedObjects.get().setMorphiaRepository(repository);
    }

    private synchronized DesiredCapabilities setProxy(ProviderConfiguration driver) {
        int port = driver.driverProvider().proxyPort();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        try {
            MobProxyExtension proxy = new MobProxyExtension(MobProxyExtension.ProxyType.WEB, port, Inet4Address.getLocalHost());
            this.webSharedObjects.get().setMobProxyExtension(proxy);
            capabilities.setCapability(CapabilityType.PROXY, this.webSharedObjects.get().getMobProxyExtension().getProxy());
            capabilities.acceptInsecureCerts();
            this.webSharedObjects.get().getMobProxyExtension().getServer().newHar();
        } catch (UnknownHostException unknownHostException) {
            throw new RuntimeException(unknownHostException);
        }
        return capabilities;
    }

    private synchronized void setDriver(ProviderConfiguration driver, DesiredCapabilities capabilities) {
        Duration duration = Duration.ofSeconds(driver.driverProvider().generalTo());
        String url = this.webSharedObjects.get().getWebConfiguration().projectUrl();
        String client = this.webSharedObjects.get().getWebConfiguration().projectClient();
        WebDriverOptions options = new WebDriverOptions();

        switch (client) {
            case "chrome" ->
                    this.webSharedObjects.get().setDriverManager(new SeleniumWebDriverManager(
                            url,
                            duration,
                            chromedriver().capabilities(options.chromeOptions().merge(capabilities)).create())
                    );
            case "firefox" ->
                    this.webSharedObjects.get().setDriverManager(new SeleniumWebDriverManager(
                            url,
                            duration,
                            firefoxdriver().capabilities(options.firefoxOptions().merge(capabilities)).create())
                    );
            default -> throw new RuntimeException("you most provide chrome or firefox driver name");
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
