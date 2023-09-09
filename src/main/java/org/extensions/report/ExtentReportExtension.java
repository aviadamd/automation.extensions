package org.extensions.report;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import lombok.extern.slf4j.Slf4j;
import org.base.configuration.PropertiesManager;
import org.base.configuration.ReportConfigurations;
import org.data.files.jsonReader.JacksonObjectAdapter;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.factory.JunitReflectionAnnotationHandler;
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
public class ExtentReportExtension implements TestWatcher,
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        JunitReflectionAnnotationHandler.ExtensionContextHandler {

    private final static List<TestInformation> failTestsCollector = new ArrayList<>();
    private final static List<TestInformation> passTestsCollector = new ArrayList<>();
    private final static List<FailTestInfoMongo> failTestsMongoCollector = new ArrayList<>();
    private final static List<PassTestInfoMongo> passTestsMongoCollector = new ArrayList<>();
    private final ThreadLocal<ReportConfigurations> reportConfigurations = new ThreadLocal<>();

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                this.reportConfigurations.set(new PropertiesManager().getOrCreate(ReportConfigurations.class));

                final String testClassName = context.getRequiredTestClass().getSimpleName();
                final String sparkLocation = this.reportConfigurations.get().reportPath() + "/Spark.html";
                final String reportConfigurationLocation = this.reportConfigurations.get().reportConfiguration();

                if (configuration.isPresent()) {
                    this.reportConfigurations.get().setProperty("project.report.path", configuration.get().reportPath());
                    this.reportConfigurations.get().setProperty("project.report.config", configuration.get().reportSettingsPath());
                    ExtentManager.createInstance(sparkLocation, reportConfigurationLocation, testClassName);
                    ExtentManager.getReportsInstance().setAnalysisStrategy(configuration.get().analysisStrategy());
                } else {
                    ExtentManager.createInstance(sparkLocation, reportConfigurationLocation, testClassName);
                    ExtentManager.getReportsInstance().setAnalysisStrategy(AnalysisStrategy.CLASS);
                }
                ExtentManager.getReportsInstance().setSystemInfo(getProperty("os.name"), getProperty("os.arch"));
            }
        } catch (Exception exception) {
            Assertions.fail("Fail init extent report ", exception);
        }
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
                final String className = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();

                if (reportTest.isPresent()) {
                    ExtentTestManager.createTest(testMethod, reportTest.get().assignCategory(), reportTest.get().assignAuthor());
                } else ExtentTestManager.createTest(testMethod, className,"unknown");
                ExtentTestManager.log(Status.INFO, "test " + testMethod + " started");
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create test with extent report ", exception);
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                final String testMethod = context.getRequiredTestMethod().getName();
                final String testStatus = context.getExecutionException().isPresent() ? "error" : "pass";
                ExtentTestManager.log(Status.INFO, "test " + testMethod + " finish with " + testStatus + " status");
            }
        } catch (Exception exception) {
            Assertions.fail("Fail finish test report with extent report ", exception);
        }
    }

    @Override
    public synchronized void testSuccessful(ExtensionContext context) {
        if (context.getElement().isPresent()) {

            final String testClass = context.getRequiredTestClass().getSimpleName();
            final String testMethod = context.getRequiredTestMethod().getName();

            List<Log> logs = this.getExtentLogs();
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);

            reportTest.ifPresent(reportInfo -> {
                TestMetaData testMetaData = this.getTestMetaData(reportInfo, logs);
                passTestsCollector.add(new TestInformation(testClass, testMetaData));
                passTestsMongoCollector.add(new PassTestInfoMongo(testClass, testMetaData));
                ExtentTestManager.log(Status.PASS, "test " + testMethod + " pass");
            });
        }
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent() && context.getExecutionException().isPresent()) {
            if (context.getElement().isPresent()) {
                final String testClass = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();
                final String errorMessage = context.getExecutionException().get().getMessage();

                List<Log> logs = this.getExtentLogs();
                Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);

                reportTest.ifPresent(reportInfo -> {
                    TestMetaData testMetaData = this.getTestMetaData(reportInfo, logs);
                    TestInformation testInformation = new TestInformation(testClass, testMetaData);
                    Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);

                    reportConfiguration.ifPresent(configuration -> {
                        Status [] repeatOnStatus = configuration.repeatOnStatus();
                        testInformation.setStatus(repeatOnStatus);
                    });

                    failTestsCollector.add(testInformation);
                    failTestsMongoCollector.add(new FailTestInfoMongo(testClass, testMetaData, errorMessage));
                    ExtentTestManager.onFail(true, FailStatus.SKIP, testMethod + " error ", throwable.getMessage());
                });
            }
        }
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent() && context.getExecutionException().isPresent()) {
            if (context.getElement().isPresent()) {
                final String testClass = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();
                final String errorMessage = context.getExecutionException().get().getMessage();

                List<Log> logs = this.getExtentLogs();
                Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);

                reportTest.ifPresent(reportInfo -> {
                    TestMetaData testMetaData = this.getTestMetaData(reportInfo, logs);
                    TestInformation testInformation = new TestInformation(testClass, testMetaData);
                    Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);

                    reportConfiguration.ifPresent(configuration -> {
                        Status [] repeatOnStatus = configuration.repeatOnStatus();
                        testInformation.setStatus(repeatOnStatus);
                    });

                    failTestsCollector.add(testInformation);
                    failTestsMongoCollector.add(new FailTestInfoMongo(testClass, testMetaData, errorMessage));
                    ExtentTestManager.onFail(true, FailStatus.FAIL, testMethod + " error ", throwable.getMessage());
                });
            }
        }
    }

    @Override
    public synchronized void testDisabled(ExtensionContext context, Optional<String> reason) {
        if (context.getElement().isPresent()) {
            final String testName = context.getRequiredTestMethod().getName();
            if (reason.isPresent()) {
                ExtentTestManager.log(Status.INFO, "test " + testName + " disabled, reason " + reason);
            } else ExtentTestManager.log(Status.INFO, "test " + testName + " disabled");
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        this.createExtraReportAndFlushReports(context);
        this.createMongoReport(context);
        this.createJsonReport(context);
    }

    private synchronized void createExtraReportAndFlushReports(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                if (configuration.isPresent() && configuration.get().extraReportsBy().length > 0) {
                    this.reportConfigurations.get().setProperty("project.report.path", configuration.get().reportPath());
                    ExtentTestManager.attachExtraReports(configuration.get().extraReportsBy(), this.reportConfigurations.get().reportPath());
                } else {
                    Status[] statuses = { Status.SKIP, Status.FAIL};
                    ExtentTestManager.attachExtraReports(statuses, this.reportConfigurations.get().reportPath());
                }
                ExtentManager.flush();
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create create extra test report with extent report ", exception);
        }
    }

    private synchronized void createMongoReport(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);
                if (reportConfiguration.isPresent()) {

                    this.reportConfigurations.get().setProperty("project.mongo.connection", reportConfiguration.get().mongoConnection());
                    final String dbName = "automationTests";

                    if (passTestsMongoCollector.size() > 0) {
                        MongoRepoImplementation mongo = new MongoRepoImplementation(this.reportConfigurations.get().mongoConnection(), dbName, "PassTestResults");
                        mongo.insertElements(PassTestAdapter.toDocuments(passTestsMongoCollector));
                        mongo.close();
                    }

                    if (failTestsMongoCollector.size() > 0) {
                        MongoRepoImplementation mongo = new MongoRepoImplementation(this.reportConfigurations.get().mongoConnection(), dbName, "FailTestResults");
                        mongo.insertElements(FailTestAdapter.toDocuments(failTestsMongoCollector));
                        mongo.close();
                    }
                }
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

                if (passTestsCollector.size() > 0) {
                    File passFilePath = new File(testPath + "/" + className + "Pass.json");
                    JacksonObjectAdapter<TestInformation> testWriter = new JacksonObjectAdapter<>(testPath, passFilePath, TestInformation.class);
                    testWriter.writeToJson(true, passTestsCollector);
                }

                if (failTestsCollector.size() > 0) {
                    File failFilePath = new File(testPath + "/" + className + "Fail.json");
                    JacksonObjectAdapter<TestInformation> testWriter = new JacksonObjectAdapter<>(testPath, failFilePath, TestInformation.class);
                    testWriter.writeToJson(true, failTestsCollector);
                }
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create create json test report ", exception);
        }
    }

    private synchronized List<Log> getExtentLogs() {
        return ExtentTestManager.getExtentTest()
                .getModel()
                .getLogs()
                .stream()
                .distinct()
                .collect(Collectors.toList());

    }

    private synchronized TestMetaData getTestMetaData(TestReportInfo reportTest, List<Log> logs) {
        return Optional.ofNullable(reportTest)
                .map(report-> new TestMetaData(report, logs))
                .orElseGet(() -> new TestMetaData("unknown", "unknown", logs));
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
