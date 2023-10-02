package org.extensions.report;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.base.RxJavaBus;
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

@Slf4j
public class ExtentReportExtension implements
        TestWatcher,
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback {

    private static final List<FailTestInfoMongo> failTestsMongoCollector = Collections.synchronizedList(new ArrayList<>());
    private static final List<PassTestInfoMongo> passTestsMongoCollector = Collections.synchronizedList(new ArrayList<>());
    private static final HashMap<Long, ReportConfigurations> propertiesMap = new HashMap<>();
    public static ReportConfigurations properties() {
        return propertiesMap.get(Thread.currentThread().getId());
    }


    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        try {
            if (context.getElement().isPresent()) {

                Optional<ReportConfiguration> configuration = this.readAnnotation(context, ReportConfiguration.class);
                if (configuration.isPresent()) {

                    propertiesMap.put(Thread.currentThread().getId(), new PropertiesManager().getOrCreate(ReportConfigurations.class));
                    final String testClassName = context.getRequiredTestClass().getSimpleName();
                    final String sparkLocation = properties().reportPath() + "/Spark.html";
                    final String reportConfigurationLocation = properties().reportConfiguration();

                    properties().setProperty("project.report.path", configuration.get().reportPath());
                    properties().setProperty("project.report.config", configuration.get().reportSettingsPath());

                    AnalysisStrategy analysisStrategy = configuration.get().analysisStrategy();
                    ExtentTestManager.getInstance().setExtentManager(sparkLocation, reportConfigurationLocation, testClassName);
                    ExtentTestManager.getInstance().setAnalysisStrategy(analysisStrategy);
                    ExtentTestManager.getInstance().setSystemInfo(System.getProperty("os.name"), System.getProperty("os.arch"));
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
                    ExtentTestManager.getInstance().createTest(testMethod, reportTest.get().assignCategory(), reportTest.get().assignAuthor());
                } else ExtentTestManager.getInstance().createTest(testMethod, className,"unknown");
                ExtentTestManager.getInstance().log(Status.INFO, " test " + testMethod + " started");
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
                ExtentTestManager.getInstance().log(Status.INFO, "test " + testMethod + " finish with " + testStatus + " status");
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
                List<Log> logs = ExtentTestManager.getInstance().getExtentLogs();
                if (logs.isEmpty()) logs.add(Log.builder().details("Test Class: " + testClass + ", Test Method " + testMethod + " Pass").build());

                ExtentTestManager.getInstance().log(Status.PASS, "test " + testMethod + " pass");
                TestMetaData testMetaData = new TestMetaData(reportInfo, logs,"");
                passTestsMongoCollector.add(new PassTestInfoMongo(new ObjectId(new Date()), testClass, testMetaData));
            });

            ObserverErrorsManager.getInstance().reset();
        }
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = this.readAnnotation(context, TestReportInfo.class);
            reportTest.ifPresent(reportInfo -> {

                final String testClass = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();

                Disposable subscribe = RxJavaBus.subscribe(object -> {
                    ExtentTestManager
                            .getInstance()
                            .onFail(true, FailStatus.SKIP, testMethod + " error ", object.toString());
                });
                subscribe.dispose();
                ExtentTestManager.getInstance().onFail(true, FailStatus.SKIP, testMethod + " error ", throwable.getMessage());

                List<Log> logs = ExtentTestManager.getInstance().getExtentLogs();
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

                Disposable subscribe = RxJavaBus.subscribe(object -> {
                    ExtentTestManager
                            .getInstance()
                            .onFail(true, FailStatus.FAIL, testMethod + " error ", object.toString());
                });
                subscribe.dispose();

                ExtentTestManager.getInstance().onFail(true, FailStatus.FAIL, testMethod + " error ", throwable.getMessage());

                List<Log> logs = ExtentTestManager.getInstance().getExtentLogs();
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
                ExtentTestManager.getInstance().log(Status.INFO, "test " + testName + " disabled, reason " + reason);
            } else ExtentTestManager.getInstance().log(Status.INFO, "test " + testName + " disabled");
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
                    properties().setProperty("project.report.path", configuration.get().reportPath());
                    ExtentTestManager.getInstance().attachExtraReports(configuration.get().extraReportsBy(), properties().reportPath());
                } else {
                    Status[] statuses = { Status.SKIP, Status.FAIL};
                    ExtentTestManager.getInstance().attachExtraReports(statuses, properties().reportPath());
                }

                ExtentTestManager.getInstance().flush();
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
                    properties().setProperty("project.mongo.connection", configuration.mongoConnection());
                    final String mongoConnection = properties().mongoConnection();

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
