package org.extensions.report;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.extensions.factory.JunitAnnotationHandler;
import org.extensions.anontations.Repeat;
import org.extensions.mongo.pojo.FailTestAdapter;
import org.extensions.mongo.pojo.PassTestAdapter;
import org.extensions.mongo.pojo.FailTestInfoMongo;
import org.extensions.mongo.pojo.PassTestInfoMongo;
import org.extensions.report.dto.FailTestInfo;
import org.extensions.report.dto.TestInformation;
import org.extensions.report.dto.TestMetaData;
import org.files.jsonReader.JsonReadAndWriteExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.mongo.legacy.MongoRepoImplementation;
import java.lang.annotation.Annotation;
import java.util.*;

@Slf4j
public class ExtentReportExtension implements TestWatcher, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {
    private static final List<FailTestInfo> failTests = new ArrayList<>();
    private static final List<TestInformation> passTests = new ArrayList<>();
    private static final List<FailTestInfoMongo> failTestsMongo = new ArrayList<>();
    private static final List<PassTestInfoMongo> passTestsMongo = new ArrayList<>();

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getElement().isPresent()) {
            try {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                if (configuration.isPresent()) {
                    String spark = System.getProperty(configuration.get().reportPath()).concat("/Spark.html");
                    String reportSettings = System.getProperty(configuration.get().reportSettingsPath()).concat("/reportConfig.json");
                    ExtentManager.createInstance(spark, reportSettings, context.getRequiredTestClass().getSimpleName());
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
            ExtentTestManager.log(Status.INFO,"test " +testMethod + " finish with status " + testStatus);
        }
    }

    @Override
    public synchronized void testSuccessful(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(testInfo -> {

                ExtentTestManager.getExtentTest().fail("details", MediaEntityBuilder.createScreenCaptureFromPath("1.png").build());
                ExtentTestManager.getExtentTest().fail("details", MediaEntityBuilder.createScreenCaptureFromPath("2.png").build());



                String testClass = context.getRequiredTestClass().getSimpleName();
                String testMethod = context.getRequiredTestMethod().getName();
                TestInformation passTestInfo = new TestInformation(new Date(), testClass, testMethod, new TestMetaData(testInfo));
                passTests.add(passTestInfo);
                PassTestInfoMongo passTestInfoMongo = new PassTestInfoMongo(new ObjectId(), testClass, testMethod, new TestMetaData(testInfo));
                passTestsMongo.add(passTestInfoMongo);
            });

            ExtentTestManager.log(Status.INFO,"test " + context.getRequiredTestMethod().getName() + " pass");
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
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
            String testMethod = context.getRequiredTestMethod().getName();

            context.getElement().ifPresent(element -> {
                Optional<Repeat> repeat = this.readAnnotation(context, Repeat.class);
                Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
                reportTest.ifPresent(testInfo -> {
                    FailTestInfo failTestInfo = new FailTestInfo(new Date(), testClass, testMethod, new TestMetaData(testInfo), error);
                    repeat.ifPresent(value -> failTestInfo.setStatus(value.onStatus()));
                    failTests.add(failTestInfo);
                    FailTestInfoMongo failTestInfoMongo = new FailTestInfoMongo(new ObjectId(), testClass, testMethod, new TestMetaData(testInfo), error);
                    failTestsMongo.add(failTestInfoMongo);
                });
            });

            ExtentTestManager.log(Status.SKIP,testClass + " class error " + error + " from test " + testMethod);
        }
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent() && context.getExecutionException().isPresent()) {

            String error = throwable.getMessage();
            String testClass = context.getRequiredTestClass().getSimpleName();
            String testMethod = context.getRequiredTestMethod().getName();

            context.getElement().ifPresent(element -> {
                Optional<Repeat> repeat = this.readAnnotation(context, Repeat.class);
                Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
                reportTest.ifPresent(testInfo -> {
                    FailTestInfo failTestInfo = new FailTestInfo(new Date(), testClass, testMethod, new TestMetaData(testInfo), error);
                    repeat.ifPresent(value -> failTestInfo.setStatus(value.onStatus()));
                    failTests.add(failTestInfo);
                    FailTestInfoMongo failTestInfoMongo = new FailTestInfoMongo(new ObjectId(), testClass, testMethod, new TestMetaData(testInfo), error);
                    failTestsMongo.add(failTestInfoMongo);
                });
            });
            ExtentTestManager.log(Status.FAIL, testClass + " class error " + error + " from test " + testMethod);
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);

            if (reportConfiguration.isPresent() && reportConfiguration.get().extraReportsBy().length > 0) {
                ExtentTestManager.attachExtraReports(reportConfiguration.get().extraReportsBy(), System.getProperty(reportConfiguration.get().reportPath()));
            }

            ExtentManager.getReportsInstance().flush();
            String className = context.getRequiredTestClass().getSimpleName();

            if (passTests.size() > 0) {
                JsonReadAndWriteExtensions write = new JsonReadAndWriteExtensions(className.concat("Pass.json"),1);
                write.readAndWrite(passTests, TestInformation.class);
            }

            if (failTests.size() > 0) {
                JsonReadAndWriteExtensions write = new JsonReadAndWriteExtensions(className.concat("Fail.json"),2);
                write.readAndWrite(failTests, FailTestInfo.class);
            }

            if (passTestsMongo.size() > 0) {
                MongoRepoImplementation mongo = new MongoRepoImplementation("mongodb://localhost:27017", "mobileTests","PassTestResults");
                List<Document> passMongoReport = PassTestAdapter.toDocuments(passTestsMongo);
                mongo.insertElements(passMongoReport);
                mongo.close();
            }

            if (failTestsMongo.size() > 0) {
                MongoRepoImplementation mongo = new MongoRepoImplementation("mongodb://localhost:27017", "mobileTests","FailTestResults");
                List<Document> failMongoReport = FailTestAdapter.toDocuments(failTestsMongo);
                mongo.insertElements(failMongoReport);
                mongo.close();
            }
        }
    }

    @Override
    public <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
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
