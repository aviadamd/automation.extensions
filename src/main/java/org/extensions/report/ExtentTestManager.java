package org.extensions.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    protected static ExtentReports getReportsInstance = ExtentManager.getReportsInstance();
    protected static ExtentSparkReporter getSparkReporterInstance() { return ExtentManager.getExtentSparkInstance(); }
    protected synchronized static ExtentTest getExtentTest() { return extentTest.get(); }
    protected synchronized static ExtentTest createTest(String testMethod, String category, String author) {
        ExtentTest test = getReportsInstance
                .createTest(testMethod)
                .createNode(testMethod)
                .assignCategory(category)
                .assignAuthor(author);
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

    public synchronized static String getScreenShot(WebDriver driver, String screenshotName) {
        String destination = "";
        try {
            String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/"+screenshotName+dateName+".png";
            File finalDestination = new File(destination);
            FileUtils.copyFile(source, finalDestination);
        } catch (Exception ignore) {}

        return destination;
    }
    public synchronized static void log(Status status, Media media) {
        getExtentTest().log(status, media);
        log.info(status + " " + media.getTitle());
    }
    public synchronized static void log(Status status, String details) {
        getExtentTest().log(status, details);
        log.info(status + " " + details);
    }
    public synchronized static void log(Status status, Markup markup) {
        getExtentTest().log(status, markup);
        log.info(status + " " + markup.getMarkup());
    }
    public synchronized static void log(Status status, Throwable throwable) {
        getExtentTest().log(status, throwable);
        log.info(status + " " + throwable.getMessage());
    }
    public synchronized static void log(Status status, String details, Media media) {
        getExtentTest().log(status, details, media);
        log.info(status + " " + details);
    }
    public synchronized static void log(Status status, Throwable throwable, Media media) {
        getExtentTest().log(status, throwable, media);
        log.info(status + " " + throwable.getMessage());
    }
    public synchronized static void log(Status status, String details, Throwable throwable, Media media) {
        getExtentTest().log(status, details, throwable, media);
        log.info(status + " " +  details + " " + throwable.getMessage());
    }

}
