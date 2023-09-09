package org.extensions.report;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.List;

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
        innerLog(status, details);
        extentTest.get().log(status, details);
    }

    /**
     * log
     * @param status
     * @param markup
     */
    public synchronized static void log(Status status, Markup markup) {
        innerLog(status," " + markup.getMarkup());
        extentTest.get().log(status, markup);
    }

    /**
     * log
     * @param status
     * @param throwable
     */
    public synchronized static void log(Status status, Throwable throwable) {
        innerLog(status, throwable.getMessage());
        extentTest.get().log(status, throwable);
    }

    /**
     * log
     * @param status
     * @param details
     * @param media
     */
    public synchronized static void log(Status status, String details, Media media) {
        innerLog(status, details);
        extentTest.get().log(status, details, media);
    }

    /**
     * log
     * @param status
     * @param throwable
     * @param media
     */
    public synchronized static void log(Status status, Throwable throwable, Media media) {
        innerLog(status, throwable.getMessage());
        extentTest.get().log(status, throwable, media);
    }

    /**
     * log
     * @param status
     * @param details
     * @param throwable
     * @param media
     */
    public synchronized static void log(Status status, String details, Throwable throwable, Media media) {
        innerLog(status, details + " " + throwable.getMessage());
        extentTest.get().log(status, details, throwable, media);
    }

    /**
     * logScreenShot
     * @param status
     * @param driver
     * @param message
     */
    public synchronized static void logScreenShot(Status status, WebDriver driver, String message, boolean createNode) {
        if (extentTest.get() != null && driver != null) {
            String base64ScreenShot = base64ScreenShot(driver);
            if (!base64ScreenShot.isEmpty()) {
                innerLog(status, message);
                Media media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64ScreenShot).build();
                if (createNode) extentTest.get()
                        .createNode("click for more details... ")
                        .log(status, message, media);
                else extentTest.get().log(status, message, media);
            }
        }
    }

    /**
     * @param status
     * @param expendMessage
     * @param bodyDesc
     * @return
     */
    public synchronized static void onFail(boolean asNewNode, FailStatus status, String expendMessage, String bodyDesc) {
        try {
            if (asNewNode) {
                extentTest.get()
                        .createNode("test error, click for more details... ")
                        .log(status.getStatus(), expendMessage)
                        .log(status.getStatus(), bodyDesc);
            } else extentTest.get().log(status.getStatus(), expendMessage + " " + bodyDesc);
        } catch (Exception ignore) {}
    }

    /**
     * @param status
     * @param expendMessage
     * @param bodyDesc
     * @return
     */
    public synchronized static void onFail(boolean asNewNode, FailStatus status, String expendMessage, List<AssertionError> bodyDesc) {
        try {
            if (asNewNode) {
                extentTest.get()
                        .createNode("test error, click for more details... ")
                        .log(status.getStatus(), expendMessage);
                bodyDesc.forEach(text -> extentTest.get().log(status.getStatus(), text.getMessage()));
            } else extentTest.get().log(status.getStatus(), expendMessage + " " + bodyDesc);
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
