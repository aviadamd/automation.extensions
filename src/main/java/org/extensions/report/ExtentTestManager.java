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
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    protected static ExtentReports getReportsInstance = ExtentManager.getReportsInstance();
    protected synchronized static ExtentTest getExtentTest() { return extentTest.get(); }

    /**
     * createTest
     * @param testMethod
     * @param category
     * @param author
     */
    protected synchronized static void createTest(String testMethod, String category, String author) {
        extentTest.set(getReportsInstance.createTest(testMethod)
                .createNode(testMethod)
                .assignCategory(category)
                .assignAuthor(author)
        );
    }

    /**
     * attachExtraReports
     * @param extraReportsBy
     * @param path
     */
    protected synchronized static void attachExtraReports(Status[] extraReportsBy, String path) {
        for (Status status : extraReportsBy) {
            String reportPath = path + "/" + status.toString() + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            getReportsInstance.attachReporter(sparkReporter.filter().statusFilter().as(new Status[]{status}).apply());
        }
    }

    /**
     * getScreenShot
     * @param driver
     * @param screenshotName
     * @return
     */
    public synchronized static String getScreenShot(WebDriver driver, String screenshotName) {
        String destination = "";
        try {
            String fileDirPath = System.getProperty("user.dir") + "/target/tests_screen_shots";
            createDir(Path.of(fileDirPath));
            destination = fileDirPath + "/" + screenshotName + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".png";
            File finalDestination = new File(destination);
            FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), finalDestination);
            return destination;
        } catch (Exception ignore) {
            return destination;
        }
    }

    /**
     * assignCategory
     * @param assignCategory
     */
    public synchronized static void assignCategory(String assignCategory) {
        extentTest.get().assignCategory(assignCategory);
    }

    /**
     * log
     * @param status
     * @param media
     */
    public synchronized static void log(Status status, Media media) {
        extentTest.get().log(status, media);
        log.info(status + " " + media.getTitle());
    }

    /**
     * log
     * @param status
     * @param details
     */
    public synchronized static void log(Status status, String details) {
        extentTest.get().log(status, details);
        log.info(status + " " + details);
    }

    /**
     * log
     * @param status
     * @param markup
     */
    public synchronized static void log(Status status, Markup markup) {
        extentTest.get().log(status, markup);
        log.info(status + " " + markup.getMarkup());
    }

    /**
     * log
     * @param status
     * @param throwable
     */
    public synchronized static void log(Status status, Throwable throwable) {
        extentTest.get().log(status, throwable);
        log.info(status + " " + throwable.getMessage());
    }

    /**
     * log
     * @param status
     * @param details
     * @param media
     */
    public synchronized static void log(Status status, String details, Media media) {
        extentTest.get().log(status, details, media);
        log.info(status + " " + details);
    }

    /**
     * log
     * @param status
     * @param throwable
     * @param media
     */
    public synchronized static void log(Status status, Throwable throwable, Media media) {
        extentTest.get().log(status, throwable, media);
        log.info(status + " " + throwable.getMessage());
    }

    /**
     * log
     * @param status
     * @param details
     * @param throwable
     * @param media
     */
    public synchronized static void log(Status status, String details, Throwable throwable, Media media) {
        extentTest.get().log(status, details, throwable, media);
        log.info(status + " " +  details + " " + throwable.getMessage());
    }

    /**
     * logScreenShot
     * @param status
     * @param driver
     * @param message
     */
    public synchronized static void logScreenShot(Status status, WebDriver driver, String message) {
        if (extentTest.get() != null && driver != null) {
            extentTest.get().log(status, ExtentTestManager.getScreenShot(driver, message));
            log.info(status + " " +  message);
        }
    }

    /**
     * createDir
     * @param path
     */
    private synchronized static void createDir(Path path) {
        try {
            Files.createDirectory(path);
        } catch (Exception ignore) {}
    }
}
