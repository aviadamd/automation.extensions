package org.extensions.report;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import lombok.extern.slf4j.Slf4j;
import org.base.configuration.PropertiesManager;
import org.base.configuration.ReportConfigurations;
import org.bson.types.ObjectId;
import org.data.files.jsonReader.JacksonExtension;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.factory.JunitAnnotationHandler;
import org.extensions.mongo.pojo.FailTestAdapter;
import org.extensions.mongo.pojo.FailTestInfoMongo;
import org.extensions.mongo.pojo.PassTestAdapter;
import org.extensions.mongo.pojo.PassTestInfoMongo;
import org.extensions.report.dto.TestInformation;
import org.extensions.report.dto.TestMetaData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.mongo.legacy.MongoRepoImplementation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.getProperty;

@Slf4j
public class ExtentReportExtension implements
        TestWatcher, BeforeAllCallback,
        BeforeEachCallback, AfterEachCallback,
        AfterAllCallback, JunitAnnotationHandler.ExtensionContextHandler {

    private final static List<TestInformation> failTests = new ArrayList<>();
    private final static List<TestInformation> passTests = new ArrayList<>();
    private final static List<FailTestInfoMongo> failTestsMongo = new ArrayList<>();
    private final static List<PassTestInfoMongo> passTestsMongo = new ArrayList<>();
    private final ThreadLocal<ReportConfigurations> reportConfigurations = new ThreadLocal<>();

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        this.createReport(context);
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        this.createTest(context);
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        this.finishTest(context);
    }

    @Override
    public synchronized void testSuccessful(ExtensionContext context) {
        this.onTestPass(context);
    }

    @Override
    public synchronized void testDisabled(ExtensionContext context, Optional<String> reason) {
        this.onTestDisabled(context, reason);
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        this.onTestSkip(context, throwable);
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        this.onTestFail(context, throwable);
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        this.createExtraReport(context);
        this.flushExtentReport(context);
        this.createMongoReport(context);
        this.createJsonReport(context);
    }


    private synchronized void onTestSkip(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent() && context.getExecutionException().isPresent()) {
            String error = throwable.getMessage();
            String testClass = context.getRequiredTestClass().getSimpleName();
            String testMethod = context.getRequiredTestMethod().getName();

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
            ExtentTestManager.onFail(true, ExtentTestManager.FailStatus.SKIP, testMethod + " error ", throwable.getMessage());
        }
    }


    private synchronized void onTestFail(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent() && context.getExecutionException().isPresent()) {
            String error = throwable.getMessage();
            String testClass = context.getRequiredTestClass().getSimpleName();
            String testMethod = context.getRequiredTestMethod().getName();

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

            ExtentTestManager.onFail(true, ExtentTestManager.FailStatus.FAIL, testMethod + " error ", throwable.getMessage());
        }
    }


    private synchronized void onTestPass(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(testInfo -> {
                String testClass = context.getRequiredTestClass().getSimpleName();
                List<Log> logs = ExtentTestManager.getExtentTest().getModel().getLogs().stream().distinct().collect(Collectors.toList());
                passTests.add(new TestInformation(new Date().toString(), testClass, new TestMetaData(testInfo, logs)));
                passTestsMongo.add(new PassTestInfoMongo(new ObjectId(), testClass, new TestMetaData(testInfo, logs)));
            });
            ExtentTestManager.log(Status.INFO, "test " + context.getRequiredTestMethod().getName() + " pass");
        }
    }


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private synchronized void onTestDisabled(ExtensionContext context, Optional<String> reason) {
        if (context.getElement().isPresent()) {
            String testName = context.getRequiredTestMethod().getName();
            if (reason.isPresent()) {
                ExtentTestManager.log(Status.INFO, "test " + testName + " disabled, reason " + reason);
            } else ExtentTestManager.log(Status.INFO, "test " + testName + " disabled");
        }
    }

    private synchronized void createReport(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                configuration.ifPresent(reportConfiguration -> {
                    this.reportConfigurations.set(new PropertiesManager().getOrCreate(ReportConfigurations.class));
                    this.reportConfigurations.get().setProperty("project.report.path", reportConfiguration.reportPath());
                    this.reportConfigurations.get().setProperty("project.report.config", reportConfiguration.reportSettingsPath());
                    final String sparkLocation = this.reportConfigurations.get().reportPath() + "/Spark.html";
                    final String reportConfigurationLocation = this.reportConfigurations.get().reportConfiguration();
                    final String testClassName = context.getRequiredTestClass().getSimpleName();
                    ExtentManager.createInstance(sparkLocation, reportConfigurationLocation, testClassName);
                    ExtentManager.getReportsInstance().setAnalysisStrategy(reportConfiguration.analysisStrategy());
                    ExtentManager.getReportsInstance().setSystemInfo(getProperty("os.name"), getProperty("os.arch"));
                });
            }
        } catch (Exception exception) {
            Assertions.fail("Fail init extent report ", exception);
        }
    }

    private synchronized void createTest(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<TestReportInfo> testReportInfo = this.readAnnotation(context, TestReportInfo.class);
                testReportInfo.ifPresent(reportInfo -> {
                    final String testMethod = context.getRequiredTestMethod().getName();
                    ExtentTestManager.createTest(testMethod, reportInfo.assignCategory(), reportInfo.assignAuthor());
                    if (!reportInfo.assignCategory().isEmpty()) ExtentTestManager.assignCategory(reportInfo.assignCategory());
                    ExtentTestManager.log(Status.INFO, "test " + testMethod + " started");
                });
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create test with extent report ", exception);
        }
    }

    private synchronized void finishTest(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                final String testMethod = context.getRequiredTestMethod().getName();
                final String testStatus = context.getExecutionException().isPresent() ? "fail" : "pass";
                ExtentTestManager.log(Status.INFO, "test " + testMethod + " finish with status " + testStatus);
            }
        } catch (Exception exception) {
            Assertions.fail("Fail finish test report with extent report ", exception);
        }
    }

    private synchronized void createExtraReport(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                if (configuration.isPresent() && configuration.get().extraReportsBy().length > 0) {
                    this.reportConfigurations.get().setProperty("project.report.path", configuration.get().reportPath());
                    ExtentTestManager.attachExtraReports(configuration.get().extraReportsBy(), this.reportConfigurations.get().reportPath());
                }
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create create extra test report with extent report ", exception);
        }
    }


    private synchronized void flushExtentReport(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            ExtentManager.flush();
        }
    }


    private synchronized void createMongoReport(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);
                reportConfiguration.ifPresent(configuration -> {

                    this.reportConfigurations.get().setProperty("project.mongo.connection", configuration.mongoConnection());
                    final String dbName = "automationTests";

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
                });
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create create mongo db report ", exception);
        }
    }


    private synchronized void createJsonReport(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                final String className = context.getRequiredTestClass().getSimpleName();
                final String testPath = System.getProperty("user.dir") + "/target/test-results";

                if (passTests.size() > 0) {
                    File passFilePath = new File(testPath + "/" + className + "Pass.json");
                    JacksonExtension<TestInformation> testWriter = new JacksonExtension<>(testPath, passFilePath, TestInformation.class);
                    testWriter.writeToJson(passTests);
                }

                if (failTests.size() > 0) {
                    File failFilePath = new File(testPath + "/" + className + "Fail.json");
                    JacksonExtension<TestInformation> testWriter = new JacksonExtension<>(testPath, failFilePath, TestInformation.class);
                    testWriter.writeToJson(failTests);
                }
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create create json test report ", exception);
        }
    }

    @Override
    public synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
        if (context.getElement().isPresent()) {
            try {
                return Optional.ofNullable(context.getElement().get().getAnnotation(annotation));
            } catch (Exception exception) {
                throw new RuntimeException("Fail read annotation from ExtentReportExtension", exception);
            }
        }
        return Optional.empty();
    }
}
