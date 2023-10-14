package org.extensions.web;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import org.base.configuration.PropertiesManager;
import org.base.web.SeleniumWebDriverManager;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.extensions.anontations.WebProviderConfiguration;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.proxy.MobProxyExtension;
import org.extensions.report.Category;
import org.extensions.report.ExtentReportExtension;
import org.extensions.report.TestData;
import org.openqa.selenium.remote.CapabilityType;
import org.utils.TestDataObserverBus;
import org.utils.files.jsonReader.FilesHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.assertions.AssertionsLevel;
import org.utils.assertions.AssertJHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class WebSharedObjectsProviderExtension extends ExtentReportExtension implements
        ParameterResolver,
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        TestWatcher {

    private final ThreadLocal<WebSharedObjects> webSharedObjects = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext extension) {
        return parameter.getParameter().getType() == WebSharedObjects.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context)  {
        if (context.getElement().isPresent()) {
            return webSharedObjects.get();
        } throw new RuntimeException("fail resolve web shared objects provider initiation");
    }

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        super.beforeAll(context);
        // Set the system property to indicate that Java 11 + Http client needs to be used.
        // By default, it uses the AsyncHttpClient.
        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        TestDataObserverBus.onNext(new TestData<>(Status.FAIL, Category.WEB, throwable.getMessage() + " " + throwable.getCause()));
        super.testFailed(context, throwable);
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        TestDataObserverBus.onNext(new TestData<>(Status.SKIP, Category.WEB, throwable.getMessage() + " " + throwable.getCause()));
        super.testFailed(context, throwable);
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        super.beforeEach(context);
        if (context.getElement().isPresent()) {
            Optional<WebProviderConfiguration> provider = this.readAnnotation(context, WebProviderConfiguration.class);
            if (provider.isPresent()) {
                this.webSharedObjects.set(new WebSharedObjects());
                this.webSharedObjects.get().setWebConfiguration(new PropertiesManager().getOrCreate(WebConfiguration.class));
                this.initAssertionManager();
                this.initDriver(provider.get().driverProvider());
            } else throw new RuntimeException("ProviderConfiguration annotation is missing in test method");
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {

            if (this.webSharedObjects.get().getMobProxyExtension() != null && this.webSharedObjects.get().getMobProxyExtension().getHar() != null) {
                Optional<WebProviderConfiguration> provider = this.readAnnotation(context, WebProviderConfiguration.class);

                provider.ifPresent(webProviderConfiguration -> {

                    String testPath = System.getProperty("user.dir") + "/target/har_files";
                    String testName = context.getRequiredTestMethod().getName();
                    new FilesHelper().createDirectory(testPath);
                    File file = new File(testPath + "/" + testName + ".json");

                    HarLog harLog = this.webSharedObjects.get()
                            .getMobProxyExtension()
                            .getHar()
                            .getLog();

                    String url = this.setNewProperty(webProviderConfiguration.driverProvider().baseUrl(), this.webSharedObjects.get().getWebConfiguration().projectUrl());
                    List<HarEntry> harEntries = harLog.getEntries()
                            .stream()
                            .filter(harEntry -> harEntry.getRequest().getUrl().equals(url))
                            .collect(Collectors.toList());

                    this.webSharedObjects.get().getMobProxyExtension().writeHarFile(file, harLog, new SimpleDateFormat("yyyy-MM-dddd:MM:ss"), harEntries, null);
                });

            }

            if (this.webSharedObjects.get().getDriverManager() != null) {
                this.webSharedObjects.get().getDriverManager().close();
            }

            if (this.webSharedObjects.get().getAssertionsManager() != null) {
                AssertionsLevel assertionsLevel = this.webSharedObjects.get().getAssertionsManager().getAssertionsLevel();
                if (assertionsLevel.equals(AssertionsLevel.HARD_AFTER_TEST))
                    this.webSharedObjects.get().getAssertionsManager().failAll();
                this.webSharedObjects.get().getAssertionsManager().setAssertionErrors(new ArrayList<>());
            }
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (this.webSharedObjects.get().getMobProxyExtension() != null) {
            this.webSharedObjects.get().getMobProxyExtension().stop();
        }
        super.afterAll(context);
    }

    private synchronized void initDriver(WebDriverType webDriverType) {
        String url = this.setNewProperty(webDriverType.baseUrl(), this.webSharedObjects.get().getWebConfiguration().projectUrl());
        String client = this.setNewProperty(webDriverType.driversInstance(), this.webSharedObjects.get().getWebConfiguration().projectClient());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        this.webSharedObjects.get().setMobProxyExtension(new MobProxyExtension());

        capabilities.setCapability(CapabilityType.PROXY, this.webSharedObjects.get().getMobProxyExtension().getProxy());
        capabilities.setAcceptInsecureCerts(true);

        SeleniumWebDriverManager driverManager = new SeleniumWebDriverManager()
                .setWaitDuration(webDriverType.durationOf(), webDriverType.fluentWaitGeneralTo())
                .setCapabilities(capabilities)
                .build(client);

        this.webSharedObjects.get().getMobProxyExtension().createNewHar();
        SeleniumWebDriverProvider webDriverProvider = new SeleniumWebDriverProvider(driverManager);
        this.webSharedObjects.get().setDriverManager(webDriverProvider);
        this.webSharedObjects.get().getDriverManager().get(url);

    }

    private synchronized void initAssertionManager() {
        this.webSharedObjects.get().setAssertionsManager(new AssertJHandler());
        this.webSharedObjects.get().getAssertionsManager().setAssertionErrors(new ArrayList<>());
        this.webSharedObjects.get().getAssertionsManager().setAssertionLevel(AssertionsLevel.HARD_AFTER_ERROR);
    }

    private synchronized String setNewProperty(String key, String value) {
        try {
            if (key.contains(".")) {
                System.setProperty(key, value);
                return System.getProperty(key);
            } else return value;
        } catch (Exception exception) {
            throw new RuntimeException("properties not valid with key: "+key+", or with value: "+value, exception);
        }
    }

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
