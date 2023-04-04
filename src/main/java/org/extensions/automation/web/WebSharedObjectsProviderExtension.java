package org.extensions.automation.web;

import com.aventstack.extentreports.Status;
import org.base.configuration.PropertiesManager;
import org.base.web.SeleniumWebDriverManager;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.extensions.anontations.ProviderConfiguration;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.factory.JunitAnnotationHandler;
import org.data.files.jsonReader.FilesHelper;
import org.extensions.report.ExtentTestManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.mongo.morphia.MorphiaRepository;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.time.Duration;
import java.util.Optional;

public class WebSharedObjectsProviderExtension implements
        ParameterResolver, BeforeAllCallback,
        BeforeEachCallback, AfterEachCallback, AfterAllCallback,
        JunitAnnotationHandler.ExtensionContextHandler, TestWatcher {
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
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent() && context.getExecutionException().isPresent()) {
            String methodName = context.getRequiredTestMethod().getName();
            ExtentTestManager.logScreenShot(Status.FAIL, webSharedObjects.get().getDriverManager().getDriver(), methodName + " fails");
        }
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent() && context.getExecutionException().isPresent()) {
            String methodName = context.getRequiredTestMethod().getName();
            ExtentTestManager.logScreenShot(Status.SKIP, webSharedObjects.get().getDriverManager().getDriver(), methodName + " fails");
        }
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<ProviderConfiguration> provider = this.readAnnotation(context, ProviderConfiguration.class);
            if (provider.isPresent()) {
                this.webSharedObjects.set(new WebSharedObjects());
                this.initWebProperties();
                this.initDriver(provider.get().driverProvider());
                this.initMongo(provider.get().dbProvider());
            } else throw new RuntimeException("ProviderConfiguration annotation is missing in test method");
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                if (this.webSharedObjects.get().getMobProxyExtension() != null && this.webSharedObjects.get().getMobProxyExtension().getServer().getHar() != null) {
                    String testPath = System.getProperty("user.dir") + "/target/har_files";
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

    private synchronized void initWebProperties() {
        try {
            PropertiesManager propertiesManager = new PropertiesManager();
            this.webSharedObjects.get().setWebConfiguration(propertiesManager.getOrCreate(WebConfiguration.class));
        } catch (Exception exception) {
            Assertions.fail("initWebProperties error " + exception, exception);
        }
    }

    private synchronized void initDriver(WebDriverType webDriverType) {
        try {
            SeleniumWebDriverManager driverManager = new SeleniumWebDriverManager();
            Duration duration = driverManager.setWebDriverWaitDuration(webDriverType.durationOf(), webDriverType.generalTo());
            String url = this.webSharedObjects.get().getWebConfiguration().projectUrl();
            String client = this.webSharedObjects.get().getWebConfiguration().projectClient();
            this.webSharedObjects.get().setMobProxyExtension(new MobProxyExtension(MobProxyExtension.ProxyType.WEB, Inet4Address.getLocalHost()));
            DesiredCapabilities capabilities = driverManager.initProxy(this.webSharedObjects.get().getMobProxyExtension());
            this.webSharedObjects.get().setDriverManager(new SeleniumWebDriverProvider(url, duration, driverManager.setWebDriver(client, capabilities)));
        } catch (Exception exception) {
            if (exception.getMessage().contains("ERR_TUNNEL_CONNECTION_FAILED")) {
                Assertions.fail("initDriver error with no Internet connection " + exception, exception);
            }
            Assertions.fail("initDriver error " + exception, exception);
        }
    }

    private synchronized void initMongo(MongoMorphiaConnector dbProvider) {
        try {
            MorphiaRepository repository = new MorphiaRepository(dbProvider.host(), dbProvider.dbName());
            this.webSharedObjects.get().setMorphiaRepository(repository);
        } catch (Exception exception) {
            Assertions.fail("initMongo error " + exception, exception);
        }
    }

    @Override
    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                if (context.getRequiredTestClass().getAnnotation(annotation) != null) {
                    return Optional.ofNullable(context.getRequiredTestClass().getAnnotation(annotation));
                } else if (context.getRequiredTestMethod().getAnnotation(annotation) != null) {
                    return Optional.ofNullable(context.getRequiredTestMethod().getAnnotation(annotation));
                } else return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                Assertions.fail("read annotation error ", exception);
            }
        }
        return Optional.empty();
    }

}
