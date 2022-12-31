package org.extensions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.extension.*;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import java.io.File;
import java.io.IOException;

public class ExtentReportListener implements
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        TestWatcher {
    public static ExtentTest extentTest;
    private ExtentReports extentReports = new ExtentReports();

    @Override
    public void testSuccessful(ExtensionContext context) {
        extentTest.pass(context.getRequiredTestMethod().getName() + " pass");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            extentTest.fail("class " + context.getRequiredTestClass().getSimpleName() + " error");
            extentTest.fail("from method " + context.getRequiredTestMethod().getName());
            if (context.getExecutionException().isPresent()) {
                extentTest.fail("error " + context.getExecutionException().get().getMessage());
            } else extentTest.fail("error " + throwable.getMessage());
        }
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws IOException {
        if (extensionContext.getElement().isPresent()) {
            ReportConfiguration configuration = extensionContext.getElement().get().getAnnotation(ReportConfiguration.class);
            this.extentReports = new ExtentReports();
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(configuration.reportPath() + "/Spark.html");
            extentReports.attachReporter(sparkReporter);
            sparkReporter.loadJSONConfig(new File(configuration.reportJsonSettingsPath()));
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            TestInfo reportTest = extensionContext.getElement().get().getAnnotation(TestInfo.class);
            extentTest = this.extentReports.createTest(extensionContext.getRequiredTestMethod().getName());
            extentTest.assignCategory(reportTest.assignCategory());
            extentTest.assignAuthor(reportTest.assignAuthor());
            extentTest.assignDevice(reportTest.assignDevice());
            extentTest.info(extensionContext.getRequiredTestMethod().getName() + " started");
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        extentTest.info("method " + context.getRequiredTestMethod().getName() + " finished");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            ReportConfiguration configuration = extensionContext.getElement().get().getAnnotation(ReportConfiguration.class);
            for (Status status: configuration.generateExtraReportsBy()) {
                if (status != Status.PASS && status != Status.INFO) {
                    ExtentSparkReporter sparkReporter = new ExtentSparkReporter(configuration.reportPath() + "/" + status.toString() + ".html");
                    this.extentReports.attachReporter(sparkReporter.filter().statusFilter().as(new Status[]{status}).apply());
                }
            }
            this.extentReports.flush();
        }
    }
}
