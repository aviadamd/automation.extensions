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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtentTestManager {
    private static final Map<Long, ExtentTest> extentTestMap = new HashMap<>();
    private static synchronized ExtentTest extentTest() {
        return extentTestMap.get(Thread.currentThread().getId());
    }

    private static ExtentTestManager m_instance;
    public static ExtentTestManager getInstance() {
        if (m_instance == null) {
            m_instance = new ExtentTestManager();
        }
        return new ExtentTestManager();
    }

    /**
     * setExtentManager
     * @param file
     * @param jsonSettingsPath
     * @param reportName
     */
    protected synchronized void setExtentManager(String file, String jsonSettingsPath, String reportName) {
        ExtentManager
                .getInstance()
                .setExtentManager(file, jsonSettingsPath, reportName);
    }

    /**
     * createTest
     * @param testMethod
     * @param category
     * @param author
     */
    protected synchronized void createTest(String testMethod, String category, String author) {
        extentTestMap.put(Thread.currentThread().getId(), ExtentManager.getInstance()
                .extentReportInstance()
                .createTest(testMethod)
                .createNode(testMethod)
                .assignCategory(category)
                .assignAuthor(author));
    }

    protected synchronized void flush() {
        ExtentManager
                .getInstance()
                .flush();
    }

    /**
     * setAnalysisStrategy
     * @param analysisStrategy
     */
    protected synchronized void setAnalysisStrategy(AnalysisStrategy analysisStrategy) {
        ExtentManager.getInstance()
                .extentReportInstance()
                .setAnalysisStrategy(analysisStrategy);
    }

    /**
     * setSystemInfo
     * @param osName
     * @param osArch
     */
    protected synchronized void setSystemInfo(String osName, String osArch) {
        ExtentManager.getInstance()
                .extentReportInstance()
                .setSystemInfo(osName, osArch);
    }

    /**
     * attachExtraReports
     * @param extraReportsBy
     * @param path
     */
    protected synchronized void attachExtraReports(Status[] extraReportsBy, String path) {
        for (Status status : extraReportsBy) {
            String reportPath = path + "/" + status.toString() + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            ExtentManager.getInstance()
                    .extentReportInstance()
                    .attachReporter(sparkReporter
                            .filter()
                            .statusFilter()
                            .as(new Status[]{status})
                            .apply());
        }
    }

    protected synchronized List<Log> getExtentLogs() {
        try {
            return extentTest()
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
    public synchronized String base64ScreenShot(WebDriver driver) {
        try {
            return ((TakesScreenshot)driver)
                    .getScreenshotAs(OutputType.BASE64);
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
    public synchronized byte[] byteScreenShot(WebDriver driver) {
        try {
            return ((TakesScreenshot)driver)
                    .getScreenshotAs(OutputType.BYTES);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * log
     * @param status
     * @param media
     */
    public synchronized void log(Status status, Media media) {
        extentTest().log(status, media);
    }

    /**
     * log
     *
     * @param status
     * @param details
     * @return
     */
    public synchronized ExtentTest log(Status status, String details) {
        return extentTest().log(status, details);
    }

    /**
     * log
     * @param status
     * @param markup
     */
    public synchronized void log(Status status, Markup markup) {
        extentTest().log(status, markup);
    }

    /**
     * log
     * @param status
     * @param throwable
     */
    public synchronized void log(Status status, Throwable throwable) {
        extentTest().log(status, throwable);
    }

    /**
     * log
     * @param status
     * @param details
     * @param media
     */
    public synchronized void log(Status status, String details, Media media) {
        extentTest().log(status, details, media);
    }

    /**
     * log
     * @param status
     * @param throwable
     * @param media
     */
    public synchronized void log(Status status, Throwable throwable, Media media) {
        extentTest().log(status, throwable, media);
    }

    /**
     * log
     * @param status
     * @param details
     * @param throwable
     * @param media
     */
    public synchronized void log(Status status, String details, Throwable throwable, Media media) {
        extentTest().log(status, details, throwable, media);
    }

    /**
     * logScreenShot
     * @param status
     * @param driver
     * @param message
     */
    public synchronized void logScreenShot(Status status, WebDriver driver, String message, boolean createNode) {
        if (extentTest() != null && driver != null) {
            String base64ScreenShot = this.base64ScreenShot(driver);
            if (!base64ScreenShot.isEmpty()) {
                Media media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64ScreenShot).build();
                if (createNode) extentTest().createNode("click for more details... ").log(status, message, media);
                else {
                    extentTest().log(status, message, media);
                }
            } else {
                extentTest().log(status, message);
            }
        }
    }

    /**
     * @param status
     * @param expendMessage
     * @param bodyDesc
     * @return
     */
    public synchronized void onFail(boolean asNewNode, FailStatus status, String expendMessage, String bodyDesc) {
        try {
            if (asNewNode) {
                extentTest().createNode("test error, click for more details... ")
                        .log(status.getStatus(), expendMessage)
                        .log(status.getStatus(), bodyDesc);
            } else extentTest().log(status.getStatus(), expendMessage + " " + bodyDesc);
        } catch (Exception ignore) {}
    }

    public synchronized <T> void onFail(boolean asNewNode, FailStatus status, String expendMessage, List<T> bodyDesc) {
        try {
            if (asNewNode) {
                ExtentTest node = extentTest()
                        .createNode("test error, click for more details... ")
                        .log(status.getStatus(), expendMessage);
                if (bodyDesc != null && !bodyDesc.isEmpty()) {
                    bodyDesc.forEach(message -> {
                        node.log(status.getStatus(), message.toString());
                    });
                }
            } else {
                extentTest().log(status.getStatus(), expendMessage + " " + bodyDesc);
            }
        } catch (Exception ignore) {}
    }
}
