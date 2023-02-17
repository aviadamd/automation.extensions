package org.extensions.automation.web;

import org.automation.configuration.PropertiesManager;
import org.automation.web.SeleniumWebDriverManager;
import org.automation.web.WebConfiguration;
import org.extensions.anontations.JacksonProvider;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.factory.JunitAnnotationHandler;
import org.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.mongo.morphia.MorphiaRepository;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;
import static io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver;

public class WebSharedObjectsProviderExtension implements
        ParameterResolver,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        JunitAnnotationHandler.ExtensionContextHandler {
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

                Optional<JacksonProvider> jackson = this.readAnnotation(context, JacksonProvider.class);
                Optional<MongoMorphiaConnector> morphia = this.readAnnotation(context, MongoMorphiaConnector.class);
                Optional<WebDriverType> driver = this.readAnnotation(context, WebDriverType.class);

                if (jackson.isPresent() && morphia.isPresent() && driver.isPresent()) {
                    this.initProvider(jackson.get(), morphia.get(), driver.get());
                } else throw new RuntimeException("fail provider initiation");

            } catch (Exception exception) {
                throw new RuntimeException("fail provider initiation", exception);
            }
        }
    }
    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                Optional<WebDriverType> driverType = this.readAnnotation(context, WebDriverType.class);
                if (driverType.isPresent()) {
                    if (this.webSharedObjects.get().getMobProxyExtension() != null && this.webSharedObjects.get().getMobProxyExtension().getServer().getHar() != null) {
                        String testPath = System.getProperty("user.dir") + "/target/harFiles";
                        String testName = context.getRequiredTestMethod().getName();
                        Files.createDirectories(Path.of(testPath));
                        File file = new File(testPath + "/" + testName + ".json");
                        this.webSharedObjects.get().getMobProxyExtension().writeHarFile(file, this.webSharedObjects.get().getMobProxyExtension().getServer().getHar().getLog());
                    }
                    this.webSharedObjects.get().getDriverManager().getDriver().quit();
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
                if (driverType.isPresent() && this.webSharedObjects.get().getMobProxyExtension().getServer() != null) {
                    this.webSharedObjects.get().getMobProxyExtension().getServer().stop();
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

    private synchronized void initProvider(JacksonProvider jackson, MongoMorphiaConnector morphia, WebDriverType driver) throws UnknownHostException {
        this.webSharedObjects.set(new WebSharedObjects());
        this.webSharedObjects.get().setWebConfiguration(new PropertiesManager().getOrCreate(WebConfiguration.class));

        switch (this.webSharedObjects.get().getWebConfiguration().projectClient()) {
            case "chrome" ->
                    this.webSharedObjects.get().setDriverManager(new SeleniumWebDriverManager(
                            this.webSharedObjects.get().getWebConfiguration().projectUrl(),
                            Duration.ofSeconds(driver.generalTo()),
                            chromedriver().capabilities(new WebDriverOptions().chromeOptions()).create()
                ));
            case "firefox" ->
                    this.webSharedObjects.get().setDriverManager(new SeleniumWebDriverManager(
                            this.webSharedObjects.get().getWebConfiguration().projectUrl(),
                            Duration.ofSeconds(driver.generalTo()),
                            firefoxdriver().capabilities(new WebDriverOptions().firefoxOptions()).create()
                ));
            default -> throw new RuntimeException("you most provide chrome or firefox driver name");
        }

        String path = System.getProperty("user.dir") + "/" + jackson.dir();
        this.webSharedObjects.get().setJacksonExtension(new JacksonExtension<>(path, new File(path + "/" + jackson.fileName()), jackson.classObject()));
        this.webSharedObjects.get().setMobProxyExtension(new MobProxyExtension(MobProxyExtension.ProxyType.WEB, driver.proxyPort(), Inet4Address.getLocalHost()));
        this.webSharedObjects.get().setMorphiaRepository(new MorphiaRepository(morphia.host(), morphia.dbName()));
    }
}
