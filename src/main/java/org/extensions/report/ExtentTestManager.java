package org.extensions.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    protected static ExtentReports getReportsInstance = ExtentManager.getReportsInstance();
    protected synchronized static ExtentTest getExtentTest() {
        return extentTest.get();
    }
    protected synchronized static ExtentTest createTest(String testMethod, String category, String author, String device) {
        ExtentTest test = getReportsInstance.createTest(testMethod)
                .createNode(testMethod)
                .assignCategory(category)
                .assignAuthor(author)
                .assignDevice(device);
        extentTest.set(test);
        return getExtentTest();
    }
    protected synchronized static void attachExtraReports(Status[] extraReportsBy, String path) {
        for (Status status : extraReportsBy) {
            String reportPath = path + "/" + status.toString() + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            getReportsInstance.attachReporter(sparkReporter.filter().statusFilter().as(new Status[]{status}).apply());
        }
    }

    public synchronized static void log(Status status, Media media) {
        getExtentTest().log(status, media);
    }
    public synchronized static void log(Status status, String details) {
        getExtentTest().log(status, details);
    }
    public synchronized static void log(Status status, Markup markup) {
        getExtentTest().log(status, markup);
    }
    public synchronized static void log(Status status, Throwable throwable) {
        getExtentTest().log(status, throwable);
    }
    public synchronized static void log(Status status, String details, Media media) { getExtentTest().log(status, details, media); }
    public synchronized static void log(Status status, Throwable throwable, Media media) { getExtentTest().log(status, throwable, media); }
    public synchronized static void log(Status status, String details, Throwable throwable, Media media) { getExtentTest().log(status, details, throwable, media); }

}
