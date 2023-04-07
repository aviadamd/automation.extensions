package org.extensions.report;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

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
     * base64ScreenShot
     * @param driver
     * @return
     */
    private synchronized static String base64ScreenShot(WebDriver driver) {
        try {
            return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * base64ScreenShot
     *
     * @param driver
     * @return
     */
    private synchronized static byte[] byteScreenShot(WebDriver driver) {
        try {
            return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception ignore) {
            return null;
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
        innerLog(status, media.getTitle());
    }

    /**
     * log
     * @param status
     * @param details
     */
    public synchronized static void log(Status status, String details) {
        extentTest.get().log(status, details);
        innerLog(status, details);
    }

    /**
     * log
     * @param status
     * @param markup
     */
    public synchronized static void log(Status status, Markup markup) {
        extentTest.get().log(status, markup);
        innerLog(status," " + markup.getMarkup());
    }

    /**
     * log
     * @param status
     * @param throwable
     */
    public synchronized static void log(Status status, Throwable throwable) {
        extentTest.get().log(status, throwable);
        innerLog(status, throwable.getMessage());
    }

    /**
     * log
     * @param status
     * @param details
     * @param media
     */
    public synchronized static void log(Status status, String details, Media media) {
        extentTest.get().log(status, details, media);
        innerLog(status, details);
    }

    /**
     * log
     * @param status
     * @param throwable
     * @param media
     */
    public synchronized static void log(Status status, Throwable throwable, Media media) {
        extentTest.get().log(status, throwable, media);
        innerLog(status, throwable.getMessage());
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
        innerLog(status, details + " " + throwable.getMessage());
    }

    /**
     * logScreenShot
     * @param status
     * @param driver
     * @param message
     */
    public synchronized static void logScreenShot(Status status, WebDriver driver, String message) {
        if (extentTest.get() != null && driver != null) {
            String base64ScreenShot = base64ScreenShot(driver);
            if (!base64ScreenShot.isEmpty()) {
                Media media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64ScreenShot).build();
                extentTest.get().log(status, message, media);
                innerLog(status, message);
            }
        }
    }

    /**
     *
     * @param status
     * @param expendMessage
     * @param bodyDesc
     */
    public synchronized static void onFail(Status status, String expendMessage, String bodyDesc) {
        try {
            if (status == Status.FAIL || status == Status.SKIP) {
                extentTest.get().createNode("test error click for more details")
                        .log(status, expendMessage)
                        .log(status, bodyDesc);
            }
        } catch (Exception ignore) {}
    }

    private synchronized static void innerLog(Status status, String details) {
        switch (status) {
            case INFO, PASS -> log.info(status + " " +  details);
            case WARNING -> log.warn(status + " " +  details);
            case SKIP, FAIL -> log.error(status + " " + details);
        }
    }
}
