package org.extensions.report;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import lombok.extern.slf4j.Slf4j;
import org.automation.configuration.PropertiesManager;
import org.automation.configuration.ReportConfigurations;
import org.bson.types.ObjectId;
import org.extensions.factory.JunitAnnotationHandler;
import org.extensions.anontations.Repeat;
import org.extensions.mongo.pojo.FailTestAdapter;
import org.extensions.mongo.pojo.PassTestAdapter;
import org.extensions.mongo.pojo.FailTestInfoMongo;
import org.extensions.mongo.pojo.PassTestInfoMongo;
import org.extensions.report.dto.TestInformation;
import org.extensions.report.dto.TestMetaData;
import org.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.mongo.legacy.MongoRepoImplementation;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import static java.lang.System.getProperty;

@Slf4j
public class ExtentReportExtension implements
        TestWatcher ,BeforeAllCallback, BeforeEachCallback,
        AfterEachCallback, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private final static List<TestInformation> failTests = new ArrayList<>();
    private final static List<TestInformation> passTests = new ArrayList<>();
    private final static List<FailTestInfoMongo> failTestsMongo = new ArrayList<>();
    private final static List<PassTestInfoMongo> passTestsMongo = new ArrayList<>();
    private final ThreadLocal<ReportConfigurations> reportConfigurations = new ThreadLocal<>();

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getElement().isPresent()) {
            try {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                if (configuration.isPresent()) {
                    this.reportConfigurations.set(new PropertiesManager().getOrCreate(ReportConfigurations.class));
                    this.reportConfigurations.get().setProperty("project.report.path", configuration.get().reportPath());
                    this.reportConfigurations.get().setProperty("project.report.config", configuration.get().reportSettingsPath());
                    ExtentManager.createInstance(this.reportConfigurations.get().reportPath() + "/Spark.html", this.reportConfigurations.get().reportConfiguration(), context.getRequiredTestClass().getSimpleName());
                    ExtentManager.getReportsInstance().setAnalysisStrategy(configuration.get().analysisStrategy());
                    ExtentManager.getReportsInstance().setSystemInfo(getProperty("os.name"), getProperty("os.arch"));
                }
            } catch (Exception exception) {
                Assertions.fail("Fail init extent report ", exception);
            }
        }
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(test -> {
                String testMethod = context.getRequiredTestMethod().getName();
                ExtentTestManager.createTest(testMethod, test.assignCategory(), test.assignAuthor());
                ExtentTestManager.log(Status.INFO, "test " + testMethod + " started");
            });
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent()) {
            String testMethod = context.getRequiredTestMethod().getName();
            String testStatus = context.getExecutionException().isPresent() ? "fail" : "pass";
            ExtentTestManager.log(Status.INFO,"test " + testMethod + " finish with status " + testStatus);
        }
    }

    @Override
    public synchronized void testSuccessful(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(testInfo -> {
                String testClass = context.getRequiredTestClass().getSimpleName();
                List<Log> logs = ExtentTestManager.getExtentTest().getModel().getLogs().stream().distinct().collect(Collectors.toList());
                passTests.add(new TestInformation(new Date().toString(), testClass, new TestMetaData(testInfo, logs)));
                passTestsMongo.add(new PassTestInfoMongo(new ObjectId(), testClass, new TestMetaData(testInfo, logs)));
            });
            ExtentTestManager.log(Status.INFO,"test " + context.getRequiredTestMethod().getName() + " pass");
        }
    }

    @Override
    public synchronized void testDisabled(ExtensionContext context, Optional<String> reason) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent()) {
            String testName = context.getRequiredTestMethod().getName();
            if (reason.isPresent()) {
                ExtentTestManager.log(Status.INFO,"test " + testName + " disabled, reason " + reason.get());
            } else ExtentTestManager.log(Status.INFO,"test " + testName + " disabled");
        }
    }
    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent() && context.getExecutionException().isPresent()) {

            String error = context.getExecutionException().get().getMessage();
            String testClass = context.getRequiredTestClass().getSimpleName();

            context.getElement().ifPresent(element -> {
                Optional<Repeat> repeat = this.readAnnotation(context, Repeat.class);
                Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
                reportTest.ifPresent(testInfo -> {
                    TestInformation failTestInfo = new TestInformation(new Date().toString(), testClass, new TestMetaData(testInfo, ExtentTestManager.getExtentTest().getModel().getLogs(), context.getExecutionException().get().getMessage()));
                    repeat.ifPresent(value -> failTestInfo.setStatus(value.onStatus()));
                    failTests.add(failTestInfo);
                    failTestsMongo.add(new FailTestInfoMongo(new ObjectId(), testClass, new TestMetaData(testInfo, context.getExecutionException().get().getMessage()), error));
                });
            });

            ExtentTestManager.log(Status.INFO,"test " + context.getRequiredTestMethod().getName() + " fails");
            ExtentTestManager.log(Status.SKIP,"error description  " + throwable.getMessage());
        }
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent() && context.getExecutionException().isPresent()) {

            String error = throwable.getMessage();
            String testClass = context.getRequiredTestClass().getSimpleName();

            context.getElement().ifPresent(element -> {
                Optional<Repeat> repeat = this.readAnnotation(context, Repeat.class);
                Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
                reportTest.ifPresent(testInfo -> {
                    TestInformation failTestInfo = new TestInformation(new Date().toString(), testClass, new TestMetaData(testInfo, ExtentTestManager.getExtentTest().getModel().getLogs(), context.getExecutionException().get().getMessage()));
                    repeat.ifPresent(value -> failTestInfo.setStatus(value.onStatus()));
                    failTests.add(failTestInfo);
                    failTestsMongo.add(new FailTestInfoMongo(new ObjectId(), testClass, new TestMetaData(testInfo, context.getExecutionException().get().getMessage()), error));
                });
            });

            ExtentTestManager.log(Status.INFO,"test " + context.getRequiredTestMethod().getName() + " fails");
            ExtentTestManager.log(Status.FAIL,"error description  " + throwable.getMessage());
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);

            if (reportConfiguration.isPresent() && reportConfiguration.get().extraReportsBy().length > 0) {
                this.reportConfigurations.get().setProperty("project.report.path", reportConfiguration.get().reportPath());
                ExtentTestManager.attachExtraReports(reportConfiguration.get().extraReportsBy(), this.reportConfigurations.get().reportPath());
            }

            String className = context.getRequiredTestClass().getSimpleName();
            ExtentManager.getReportsInstance().flush();

            String testPath = System.getProperty("user.dir") + "/target/testResults";

            if (passTests.size() > 0) {
                JacksonExtension<TestInformation> testWriter = new JacksonExtension<>(testPath, className.concat("Pass.json"), TestInformation.class);
                testWriter.writeToJson(passTests);
            }

            if (failTests.size() > 0) {
                JacksonExtension<TestInformation> testWriter = new JacksonExtension<>(testPath, className.concat("Fail.json"), TestInformation.class);
                testWriter.writeToJson(passTests);
            }

            if (reportConfiguration.isPresent() && !reportConfiguration.get().mongoConnection().isEmpty()) {
                this.reportConfigurations.get().setProperty("project.mongo.connection", reportConfiguration.get().mongoConnection());
                String dbName = "mobileTests";
                if (passTestsMongo.size() > 0) {
                    MongoRepoImplementation mongo = new MongoRepoImplementation(this.reportConfigurations.get().mongoConnection(), dbName, "PassTestResults");
                    mongo.insertElements(PassTestAdapter.toDocuments(passTestsMongo));
                    mongo.close();
                }

                if (failTestsMongo.size() > 0) {
                    MongoRepoImplementation mongo = new MongoRepoImplementation(this.reportConfigurations.get().mongoConnection(), dbName, "FailTestResults");
                    mongo.insertElements(FailTestAdapter.toDocuments(failTestsMongo));
                    mongo.close();
                }
            }
        }
    }

    @Override
    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                Assertions.fail("Fail read annotation from ExtentReportExtension", exception);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
