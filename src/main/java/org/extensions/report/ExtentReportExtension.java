package org.extensions.report;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import org.base.ErrorsCollector;
import org.base.configuration.PropertiesManager;
import org.base.anontations.ReportConfigurations;
import org.bson.types.ObjectId;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.mongo.pojo.FailTestAdapter;
import org.extensions.mongo.pojo.FailTestInfoMongo;
import org.extensions.mongo.pojo.PassTestAdapter;
import org.extensions.mongo.pojo.PassTestInfoMongo;
import org.extensions.report.dto.TestMetaData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.mongo.legacy.MongoRepoImplementation;
import java.lang.annotation.Annotation;
import java.util.*;

public class ExtentReportExtension implements
        TestWatcher,
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback {
    private static final List<FailTestInfoMongo> failTestsMongoCollector = Collections.synchronizedList(new ArrayList<>());
    private static final List<PassTestInfoMongo> passTestsMongoCollector = Collections.synchronizedList(new ArrayList<>());
    private final ThreadLocal<ReportConfigurations> reportConfigurations = new ThreadLocal<>();

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                if (configuration.isPresent()) {

                    this.reportConfigurations.set(new PropertiesManager().getOrCreate(ReportConfigurations.class));

                    final String testClassName = context.getRequiredTestClass().getSimpleName();
                    final String sparkLocation = this.reportConfigurations.get().reportPath() + "/Spark.html";
                    final String reportConfigurationLocation = this.reportConfigurations.get().reportConfiguration();

                    this.reportConfigurations.get().setProperty("project.report.path", configuration.get().reportPath());
                    this.reportConfigurations.get().setProperty("project.report.config", configuration.get().reportSettingsPath());

                    AnalysisStrategy analysisStrategy = configuration.get().analysisStrategy();
                    ExtentManager.setExtentManager(sparkLocation, reportConfigurationLocation, testClassName);
                    ExtentTestManager.setAnalysisStrategy(analysisStrategy);
                    ExtentTestManager.setSystemInfo(System.getProperty("os.name"), System.getProperty("os.arch"));
                }
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
                ErrorsCollector.getInstance().setObservableCollector(new ArrayList<>());
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
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);

            reportTest.ifPresent(reportInfo -> {
                List<Log> logs = ExtentTestManager.getExtentLogs();
                if (logs.isEmpty()) logs.add(Log.builder().details("Test Class: " + testClass + ", Test Method " + testMethod + " Pass").build());

                ExtentTestManager.log(Status.PASS, "test " + testMethod + " pass");

                TestMetaData testMetaData = new TestMetaData(reportInfo, logs,"");
                passTestsMongoCollector.add(new PassTestInfoMongo(new ObjectId(new Date()), testClass, testMetaData));
            });
        }
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(reportInfo -> {

                final String testClass = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();

                List<Object> collector = ErrorsCollector.getInstance().getCollector();
                ExtentTestManager.onFail(true, FailStatus.SKIP, testMethod + " error ", collector.toString());

                List<Log> logs = ExtentTestManager.getExtentLogs();
                if (logs.isEmpty()) logs.add(Log.builder().details(throwable.getMessage()).build());

                TestMetaData testMetaData = new TestMetaData(reportInfo, logs, throwable.getMessage());
                failTestsMongoCollector.add(new FailTestInfoMongo(new ObjectId(new Date()), testClass, testMetaData, throwable.getMessage()));

            });
        }
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(reportInfo -> {
                final String testClass = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();

                List<Object> collector = ErrorsCollector.getInstance().getCollector();
                if (collector.size() > 0) {
                    ExtentTestManager.onFail(true, FailStatus.FAIL, testMethod + " error ", collector);
                } else ExtentTestManager.onFail(true, FailStatus.FAIL, testMethod + " error ", throwable.getMessage());

                List<Log> logs = ExtentTestManager.getExtentLogs();
                if (logs.isEmpty()) logs.add(Log.builder().details(throwable.getMessage()).build());

                TestMetaData testMetaData = new TestMetaData(reportInfo, logs, throwable.getMessage());
                failTestsMongoCollector.add(new FailTestInfoMongo(new ObjectId(new Date()), testClass, testMetaData, throwable.getMessage()));
            });
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
        this.createMongoReport(context);
        this.createExtraReportAndFlushReports(context);
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
                ExtentTestManager.remove();
            }
        } catch (Exception exception) {
            Assertions.fail("Fail create create extra test report with extent report ", exception);
        }
    }

    private synchronized void createMongoReport(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {
                Optional<ReportConfiguration> reportConfiguration = this.readAnnotation(context, ReportConfiguration.class);
                reportConfiguration.ifPresent(configuration -> {
                    this.reportConfigurations.get().setProperty("project.mongo.connection", configuration.mongoConnection());
                    final String mongoConnection = this.reportConfigurations.get().mongoConnection();

                    MongoRepoImplementation mongo;
                    if (passTestsMongoCollector.size() > 0) {
                        mongo = new MongoRepoImplementation(mongoConnection, configuration.mongoDbName(), "PassTestResults");
                        mongo.insertElements(PassTestAdapter.toDocuments(passTestsMongoCollector));
                        mongo.close();
                    }

                    if (failTestsMongoCollector.size() > 0) {
                        mongo = new MongoRepoImplementation(mongoConnection, configuration.mongoDbName(), "FailTestResults");
                        mongo.insertElements(FailTestAdapter.toDocuments(failTestsMongoCollector));
                        mongo.close();
                    }
                });
            }
        } catch (Exception ignore) {}
    }

    private synchronized <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation) {
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
