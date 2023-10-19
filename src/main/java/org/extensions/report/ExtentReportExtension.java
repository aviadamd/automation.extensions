package org.extensions.report;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Log;
import io.reactivex.rxjava3.disposables.Disposable;
import org.utils.TestDataObserverBus;
import org.base.configuration.PropertiesManager;
import org.base.anontations.ReportConfigurations;
import org.bson.types.ObjectId;
import org.utils.date.DateTimeUtilExtension;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.mongo.pojo.TestInfoAdapter;
import org.extensions.mongo.pojo.TestInfoMongo;
import org.junit.jupiter.api.extension.*;
import org.utils.mongo.legacy.MongoRepoImplementation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class ExtentReportExtension implements
        TestWatcher,
        BeforeAllCallback,
        AfterEachCallback,
        BeforeEachCallback,
        AfterAllCallback {

    private static final HashMap<Long, ReportConfigurations> propertiesMap = new HashMap<>();
    private static ReportConfigurations properties() {
        return propertiesMap.get(Thread.currentThread().getId());
    }
    private static final List<TestInfoMongo> testInfoCollector = Collections.synchronizedList(new ArrayList<>());


    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<ReportSetUp> reportSetUp = Optional.ofNullable(context.getRequiredTestClass().getAnnotation(ReportSetUp.class));
            if (reportSetUp.isPresent()) {
                propertiesMap.put(Thread.currentThread().getId(), new PropertiesManager().getOrCreate(ReportConfigurations.class));
                String testClassName = context.getRequiredTestClass().getSimpleName();
                String sparkLocation = properties().reportPath() + "/Spark.html";
                String reportConfigurationLocation = properties().reportConfiguration();

                properties().setProperty("project.report.path", reportSetUp.get().reportPath());
                properties().setProperty("project.report.config", reportSetUp.get().reportSettingsPath());

                AnalysisStrategy analysisStrategy = reportSetUp.get().analysisStrategy();
                ExtentManager.createInstance(sparkLocation, reportConfigurationLocation, testClassName);
                ExtentTestManager.setAnalysisStrategy(analysisStrategy);
                ExtentTestManager.setSystemInfo(System.getProperty("os.name"), System.getProperty("os.arch"));
            }
        }
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = Optional.ofNullable(context.getRequiredTestMethod().getAnnotation(TestReportInfo.class));
            final String testMethod = context.getRequiredTestMethod().getName();
            reportTest.ifPresent(reportInfo -> {
                ExtentTestManager.createTest(testMethod, reportInfo.assignCategory(), reportInfo.assignAuthor());
                ExtentTestManager.log(Status.INFO, MarkupHelper.createLabel("test " + testMethod + " started", ExtentColor.GREY));
            });
        }
    }

    @Override
    public synchronized void testSuccessful(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            final String testClass = context.getRequiredTestClass().getSimpleName();
            final String testMethod = context.getRequiredTestMethod().getName();
            Optional<TestReportInfo> reportTest = Optional.ofNullable(context.getRequiredTestMethod().getAnnotation(TestReportInfo.class));
            reportTest.ifPresent(reportInfo -> {
                ExtentTestManager.log(Status.PASS, "test " + testMethod + " pass");
                Optional<ReportSetUp> reportConfiguration = Optional.ofNullable(context.getRequiredTestClass().getAnnotation(ReportSetUp.class));
                List<Log> logs = ExtentTestManager.getExtentLogs();
                reportConfiguration.ifPresent(configuration -> testInfoCollector.add(new TestInfoMongo(new ObjectId(new Date()), testClass, Status.PASS, reportInfo.assignCategory(), reportInfo.assignAuthor(), logs)));
            });
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = Optional.ofNullable(context.getRequiredTestMethod().getAnnotation(TestReportInfo.class));
            reportTest.ifPresent(reportInfo -> {
                final String testMethod = context.getRequiredTestMethod().getName();
                ExtentTestManager.log(Status.INFO, "test " + testMethod + " finished");
            });
        }
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = Optional.ofNullable(context.getRequiredTestMethod().getAnnotation(TestReportInfo.class));
            reportTest.ifPresent(reportInfo -> {
                final String testClass = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();

                List<Log> logs = ExtentTestManager.getExtentLogs();
                logs.add(Log.builder().details(throwable.getMessage()).status(Status.SKIP).build());

                Disposable disposable = TestDataObserverBus.subscribe(onNext -> {
                    if (onNext.size() > 0) {
                        onNext.stream().distinct().forEach(action -> {
                            logs.add(Log.builder().details(action.toString()).status(Status.SKIP).build());
                            ExtentTestManager.onFail(FailStatus.SKIP , testMethod + " error ", action.toString());
                        });
                    } else ExtentTestManager.onFail(FailStatus.SKIP, testMethod + " error ", throwable.getMessage());
                });
                disposable.dispose();

                Optional<ReportSetUp> reportSetUp = Optional.ofNullable(context.getRequiredTestClass().getAnnotation(ReportSetUp.class));
                reportSetUp.ifPresent(configuration -> testInfoCollector.add(new TestInfoMongo(new ObjectId(new Date()), testClass, Status.SKIP, reportInfo.assignCategory(), reportInfo.assignAuthor(), logs)));
            });
        }
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = Optional.ofNullable(context.getRequiredTestMethod().getAnnotation(TestReportInfo.class));
            reportTest.ifPresent(reportInfo -> {
                final String testClass = context.getRequiredTestClass().getSimpleName();
                final String testMethod = context.getRequiredTestMethod().getName();

                List<Log> logs = ExtentTestManager.getExtentLogs();
                logs.add(Log.builder().details(throwable.getMessage()).status(Status.FAIL).build());

                Disposable disposable = TestDataObserverBus.subscribe(onNext -> {
                    if (onNext.size() > 0) {
                        ExtentTestManager.onFail(FailStatus.FAIL, testMethod + " error ", onNext.stream().distinct().collect(Collectors.toList()));
                    } else ExtentTestManager.onFail(FailStatus.FAIL, testMethod + " error ", throwable.getMessage());
                });
                disposable.dispose();

                Optional<ReportSetUp> reportSetUp = Optional.ofNullable(context.getRequiredTestClass().getAnnotation(ReportSetUp.class));
                reportSetUp.ifPresent(configuration -> testInfoCollector.add(new TestInfoMongo(new ObjectId(new Date()), testClass, Status.FAIL, reportInfo.assignCategory(), reportInfo.assignAuthor(), logs)));
            });
        }
    }

    @Override
    public synchronized void testDisabled(ExtensionContext context, Optional<String> reason) {
        if (context.getElement().isPresent()) {
            Optional<TestReportInfo> reportTest = Optional.ofNullable(context.getRequiredTestMethod().getAnnotation(TestReportInfo.class));
            reportTest.ifPresent(reportInfo -> {
                final String testName = context.getRequiredTestMethod().getName();
                if (reason.isPresent()) {
                    ExtentTestManager.log(Status.INFO, "test " + testName + " disabled, reason " + reason);
                } else ExtentTestManager.log(Status.INFO, "test " + testName + " disabled");
            });
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            Optional<ReportSetUp> reportSetUp = Optional.ofNullable(context.getRequiredTestClass().getAnnotation(ReportSetUp.class));
            if (reportSetUp.isPresent()) {
                TestDataObserverBus.reset();
                this.addExtentExtraReports(reportSetUp.get());
                ExtentManager.flush();

                String className = context.getRequiredTestClass().getSimpleName();
                this.addMongoDbReport(reportSetUp.get(), className);
            }
        }
    }

    private synchronized void addExtentExtraReports(ReportSetUp reportSetUp) {
        if (reportSetUp.extraReportsBy().length > 0) {
            properties().setProperty("project.report.path", reportSetUp.reportPath());
            ExtentTestManager.attachExtraReports(reportSetUp.extraReportsBy(), properties().reportPath());
        }
    }

    private synchronized void addMongoDbReport(ReportSetUp reportSetUp, String className) {
        properties().setProperty("project.mongo.connection", reportSetUp.mongoConnection());
        String mongoConnection = properties().mongoConnection();
        DateTimeUtilExtension dateTimeUtilExtension = new DateTimeUtilExtension();
        LocalDate localDate = dateTimeUtilExtension.buildDate(LocalDate.now(), LocalTime.now(), ZoneOffset.UTC);
        String date =  dateTimeUtilExtension.dateToString("dd MMMM yyyy", localDate, Locale.CANADA, null, null);
        MongoRepoImplementation mongo = new MongoRepoImplementation(mongoConnection, reportSetUp.mongoDbName(), className + "_" + date);
        mongo.insertElements(TestInfoAdapter.toDocuments(ExtentReportExtension.testInfoCollector));
        mongo.close();
    }
}
