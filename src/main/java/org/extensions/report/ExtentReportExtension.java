package org.extensions.report;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import lombok.extern.slf4j.Slf4j;
import org.automation.AutomationProperties;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.extensions.factory.JunitAnnotationHandler;
import org.extensions.anontations.Repeat;
import org.extensions.mongo.pojo.FailTestAdapter;
import org.extensions.mongo.pojo.PassTestAdapter;
import org.extensions.mongo.pojo.FailTestInfoMongo;
import org.extensions.mongo.pojo.PassTestInfoMongo;
import org.extensions.report.dto.TestInformation;
import org.extensions.report.dto.TestMetaData;
import org.files.jsonReader.JacksonExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.mongo.legacy.MongoRepoImplementation;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ExtentReportExtension implements TestWatcher, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private static final List<TestInformation> failTests = new ArrayList<>();
    private static final List<TestInformation> passTests = new ArrayList<>();
    private static final List<FailTestInfoMongo> failTestsMongo = new ArrayList<>();
    private static final List<PassTestInfoMongo> passTestsMongo = new ArrayList<>();

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getElement().isPresent()) {
            try {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                if (configuration.isPresent()) {
                    String spark = AutomationProperties.getInstance().getProperty(configuration.get().reportPath());
                    String reportSettings = AutomationProperties.getInstance().getProperty(configuration.get().reportSettingsPath());
                    ExtentManager.createInstance(spark + "/Spark.html", reportSettings, context.getRequiredTestClass().getSimpleName());
                    ExtentManager.getReportsInstance().setAnalysisStrategy(configuration.get().analysisStrategy());
                    ExtentManager.getReportsInstance().setSystemInfo(System.getProperty("os.name"), System.getProperty("os.arch"));
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
                TestInformation passTestInfo = new TestInformation(new Date().toString(), testClass, new TestMetaData(testInfo, logs));
                passTests.add(passTestInfo);
                PassTestInfoMongo passTestInfoMongo = new PassTestInfoMongo(new ObjectId(), testClass, new TestMetaData(testInfo, logs));
                passTestsMongo.add(passTestInfoMongo);
            });
            ExtentTestManager.log(Status.INFO,"test " + context.getRequiredTestMethod().getName() + " pass");
        }
    }

    @Override
    public synchronized void testDisabled(ExtensionContext context, Optional<String> reason) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent()) {
            if (reason.isPresent()) {
                ExtentTestManager.log(Status.INFO,"test " + context.getRequiredTestMethod().getName() + " disabled, reason " + reason.get());
            } else ExtentTestManager.log(Status.INFO,"test " + context.getRequiredTestMethod().getName() + " disabled");
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
                    FailTestInfoMongo failTestInfoMongo = new FailTestInfoMongo(new ObjectId(), testClass, new TestMetaData(testInfo, context.getExecutionException().get().getMessage()), error);
                    failTestsMongo.add(failTestInfoMongo);
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
                    FailTestInfoMongo failTestInfoMongo = new FailTestInfoMongo(new ObjectId(), testClass, new TestMetaData(testInfo, context.getExecutionException().get().getMessage()), error);
                    failTestsMongo.add(failTestInfoMongo);
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
                ExtentTestManager.attachExtraReports(
                        reportConfiguration.get().extraReportsBy(),
                        AutomationProperties.getInstance().getProperty(reportConfiguration.get().reportPath()));
            }

            String className = context.getRequiredTestClass().getSimpleName();
            ExtentManager.getReportsInstance().flush();

            if (passTests.size() > 0) {
                JacksonExtensions write = new JacksonExtensions(className.concat("Pass.json"),1);
                write.readAndWrite(passTests, TestInformation.class, true);
            }

            if (failTests.size() > 0) {
                JacksonExtensions write = new JacksonExtensions(className.concat("Fail.json"),2);
                write.readAndWrite(failTests, TestInformation.class, true);
            }

            if (reportConfiguration.isPresent() && !reportConfiguration.get().mongoConnection().isEmpty()) {
                String dbName = "mobileTests";
                if (passTestsMongo.size() > 0) {
                    MongoRepoImplementation mongo = new MongoRepoImplementation(AutomationProperties.getInstance().getProperty(reportConfiguration.get().mongoConnection()), dbName, "PassTestResults");
                    List<Document> passMongoReport = PassTestAdapter.toDocuments(passTestsMongo);
                    mongo.insertElements(passMongoReport);
                    mongo.close();
                }

                if (failTestsMongo.size() > 0) {
                    MongoRepoImplementation mongo = new MongoRepoImplementation(AutomationProperties.getInstance().getProperty(reportConfiguration.get().mongoConnection()), dbName, "FailTestResults");
                    List<Document> failMongoReport = FailTestAdapter.toDocuments(failTestsMongo);
                    mongo.insertElements(failMongoReport);
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
