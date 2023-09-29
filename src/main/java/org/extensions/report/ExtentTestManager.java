package org.extensions.report;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(ExtentTestManager.class);
    protected static void remove() {
        extentTest.remove();
    }

    /**
     * createTest
     * @param testMethod
     * @param category
     * @param author
     */
    protected synchronized static void createTest(String testMethod, String category, String author) {
        extentTest.set(ExtentManager.extentReportInstance()
                .createTest(testMethod)
                .createNode(testMethod)
                .assignCategory(category)
                .assignAuthor(author));
    }

    /**
     * setAnalysisStrategy
     * @param analysisStrategy
     */
    protected synchronized static void setAnalysisStrategy(AnalysisStrategy analysisStrategy) {
        ExtentManager.extentReportInstance().setAnalysisStrategy(analysisStrategy);
    }

    /**
     * setSystemInfo
     * @param osName
     * @param osArch
     */
    protected synchronized static void setSystemInfo(String osName, String osArch) {
        ExtentManager.extentReportInstance().setSystemInfo(osName, osArch);
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
            ExtentManager.extentReportInstance()
                    .attachReporter(sparkReporter
                            .filter()
                            .statusFilter()
                            .as(new Status[]{status})
                            .apply());
        }
    }

    public static synchronized List<Log> getExtentLogs() {
        try {
            return extentTest.get()
                    .getModel()
                    .getLogs()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            return new ArrayList<>();
        }
    }

    /**
     * base64ScreenShot
     * @param driver
     * @return
     */
    public synchronized static String base64ScreenShot(WebDriver driver) {
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
    public synchronized static byte[] byteScreenShot(WebDriver driver) {
        try {
            return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception ignore) {
            return null;
        }
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
            } else {
                extentTest.get().log(status, message);
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

    public synchronized static <T> void onFail(boolean asNewNode, FailStatus status, String expendMessage, List<T> bodyDesc) {
        try {
            if (asNewNode) {
                ExtentTest node = extentTest.get().createNode("test error, click for more details... ");
                node.log(status.getStatus(), expendMessage);
                bodyDesc.forEach(message -> node.log(status.getStatus(), message.toString()));
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
