package org.extensions.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.extensions.anontations.Repeat;
import org.extensions.dto.*;
import org.extensions.mongo.FailTestAdapter;
import org.extensions.mongo.PassTestAdapter;
import org.extensions.mongo.pojo.FailTestInfoMongo;
import org.extensions.mongo.pojo.PassTestInfoMongo;
import org.files.jsonReader.JsonReadAndWriteExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.mongo.legacy.MongoRepoImplementation;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;
import static com.aventstack.extentreports.reporter.configuration.ViewName.*;

@Slf4j
@Component
@Configuration
public class ExtentReportExtension implements
        TestWatcher,
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        JunitAnnotationHandler.ExtensionContextHandler {

    public static ExtentTest extentTest;
    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extentReports = new ExtentReports();
    private static final List<FailTestInfo> failTests = new ArrayList<>();
    private static final List<PassTestInfo> passTests = new ArrayList<>();
    private static final List<FailTestInfoMongo> failTestsMongo = new ArrayList<>();
    private static final List<PassTestInfoMongo> passTestsMongo = new ArrayList<>();

    @Override
    public void beforeAll(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getElement().isPresent()) {
            Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);
            if (reportConfiguration.isPresent()) {
                try {
                    extentReports = new ExtentReports();
                    sparkReporter = new ExtentSparkReporter(reportConfiguration.get().reportPath() + "/Spark.html");
                    sparkReporter.viewConfigurer()
                            .viewOrder()
                            .as(new ViewName[] { DASHBOARD, TEST, AUTHOR, DEVICE, EXCEPTION, LOG})
                            .apply();
                    extentReports.attachReporter(sparkReporter);
                    sparkReporter.loadJSONConfig(new File(reportConfiguration.get().reportSettingsPath()));
                } catch (Exception exception) {
                    Assertions.fail("Extent report initiation error ", exception);
                }
            }
        }
    }
    @Override
    public void beforeEach(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            if (reportTest.isPresent()) {
                String testClass = context.getRequiredTestClass().getSimpleName();
                String testMethod = context.getRequiredTestMethod().getName();
                extentTest = extentReports
                        .createTest(testMethod)
                        .createNode(testMethod)
                        .assignCategory(reportTest.get().assignCategory())
                        .assignAuthor(reportTest.get().assignAuthor())
                        .assignDevice(reportTest.get().assignDevice());
                extentTest.info("class " + testClass + " started");
                extentTest.info("test " + testMethod + " started");
            }
        }
    }
    @Override
    public void afterEach(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent()) {
            String testMethod = context.getRequiredTestMethod().getName();
            String testStatus = context.getExecutionException().isPresent() ? "fail" : "pass";
            extentTest.info("test " + testMethod + " finished with " + testStatus + " status");
        }
    }
    @Override
    public void testSuccessful(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(testInfo -> {
                String testClass = context.getRequiredTestClass().getSimpleName();
                String testMethod = context.getRequiredTestMethod().getName();
                PassTestInfo passTestInfo = new PassTestInfo(new Date(), testClass, testMethod, new TestMetaData(testInfo));
                passTests.add(passTestInfo);
                PassTestInfoMongo passTestInfoMongo = new PassTestInfoMongo(new ObjectId(), testClass, testMethod, new TestMetaData(testInfo));
                passTestsMongo.add(passTestInfoMongo);
            });

            String testMethod = context.getRequiredTestMethod().getName();
            extentTest.pass("test " + testMethod + " pass");
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getExecutionException().isPresent()) {

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

            extentTest.info("fail from class " + testClass);
            extentTest.info("fail from method " + testMethod);
            extentTest.fail("error message " + error);
        }
    }
    @Override
    public void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);

            if (reportConfiguration.isPresent() && reportConfiguration.get().extraReportsBy().length > 0) {
                for (Status status : reportConfiguration.get().extraReportsBy()) {
                    String reportPath = reportConfiguration.get().reportPath() + "/" + status.toString() + ".html";
                    sparkReporter = new ExtentSparkReporter(reportPath);
                    extentReports.attachReporter(sparkReporter.filter().statusFilter().as(new Status[]{status}).apply());
                }
            }

            extentReports.flush();
            String className = context.getRequiredTestClass().getSimpleName();

            if (passTests.size() > 0) {
                JsonReadAndWriteExtensions write = new JsonReadAndWriteExtensions(className.concat("Pass.json"),1);
                write.readAndWrite(passTests, PassTestInfo.class);
            }

            if (failTests.size() > 0) {
                JsonReadAndWriteExtensions write = new JsonReadAndWriteExtensions(className.concat("Fail.json"),2);
                write.readAndWrite(failTests, FailTestInfo.class);
            }

            if (passTestsMongo.size() > 0) {
                MongoRepoImplementation mongo = new MongoRepoImplementation("mongodb://localhost:27017","testResults","PassTestResults");
                List<Document> passMongoReport = PassTestAdapter.toDocuments(passTestsMongo);
                mongo.insertElements(passMongoReport);
                mongo.close();
            }

            if (failTestsMongo.size() > 0) {
                MongoRepoImplementation mongo = new MongoRepoImplementation("mongodb://localhost:27017","testResults","FailTestResults");
                List<Document> failMongoReport = FailTestAdapter.toDocuments(failTestsMongo);
                mongo.insertElements(failMongoReport);
                mongo.close();
            }
        }
    }
    @Override
    public  <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
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
