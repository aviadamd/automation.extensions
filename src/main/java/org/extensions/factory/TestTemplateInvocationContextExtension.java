package org.extensions.factory;

import org.automation.web.SeleniumWebDriverManager;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.report.ExtentReportExtension;
import org.files.jsonReader.JacksonWriterExtension;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Extensions(value = @ExtendWith(value = ExtentReportExtension.class))
public class TestTemplateInvocationContextExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        HashMap<Long, SeleniumWebDriverManager> webDriverManager = new HashMap<>();

        return Stream.of(new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return "";
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Arrays.asList(
                        new ParameterResolver() {
                            @Override
                            public boolean supportsParameter(ParameterContext parameter, ExtensionContext context)  {
                                return parameter.getParameter().getType() == SeleniumWebDriverManager.class;
                            }

                            @Override
                            public Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
                                if (context.getElement().isPresent()) {
                                    Optional<WebDriverType> driverType = Optional.ofNullable(context.getElement().get().getAnnotation(WebDriverType.class));
                                    if (driverType.isPresent()) {
                                        for (Class<? extends WebDriver> instance : driverType.get().driversInstance()) {
                                            webDriverManager.put(Thread.currentThread().getId(), new SeleniumWebDriverManager(driverType.get().isWithProxy(), System.getProperty(driverType.get().baseUrl()), instance, Duration.ofSeconds(driverType.get().generalTo())));
                                        }
                                    }
                                }
                                return webDriverManager.get(Thread.currentThread().getId());
                            }
                        },
                        (AfterEachCallback) context -> {
                            if (context.getElement().isPresent()) {
                                try {
                                    Optional<WebDriverType> driverType = Optional.ofNullable(context.getElement().get().getAnnotation(WebDriverType.class));
                                    if (driverType.isPresent()) {
                                        if (webDriverManager.get(Thread.currentThread().getId()).getBrowserMobProxy() != null && webDriverManager.get(Thread.currentThread().getId()).getBrowserMobProxy().getHar() != null) {
                                            JacksonWriterExtension write = new JacksonWriterExtension(new File(System.getProperty("user.dir") + "/target/" + context.getRequiredTestMethod().getName() + ".json"));
                                            write.writeToJson(webDriverManager.get(Thread.currentThread().getId()).getBrowserMobProxy().getHar(), false);
                                        }
                                    }
                                } catch (Exception ignore) {}
                            }
                        },

                        (AfterAllCallback) context -> {
                            if (context.getElement().isPresent()) {
                                try {
                                    Optional<WebDriverType> driverType = Optional.ofNullable(context.getElement().get().getAnnotation(WebDriverType.class));
                                    if (driverType.isPresent()) {
                                        if (webDriverManager.get(Thread.currentThread().getId()).getBrowserMobProxy() != null) {
                                            webDriverManager.get(Thread.currentThread().getId()).getBrowserMobProxy().stop();
                                        }
                                    }
                                } catch (Exception ignore) {}
                            }
                        }
                );
            }
        });
    }

}
