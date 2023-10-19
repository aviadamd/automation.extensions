package org.extensions.report;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.http.Header;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    protected static ExtentReports getReportsInstance = ExtentManager.getReportsInstance();
    protected synchronized static ExtentTest getExtentTest() { return extentTest.get(); }


    protected synchronized static void createTest(String testMethod, String category, String author) {
        ExtentTest test = getReportsInstance
                .createTest(testMethod)
                .createNode(testMethod)
                .assignCategory(category)
                .assignAuthor(author);
        extentTest.set(test);
    }


    /**
     * setAnalysisStrategy
     * @param analysisStrategy
     */
    protected synchronized static void setAnalysisStrategy(AnalysisStrategy analysisStrategy) {
        getReportsInstance.setAnalysisStrategy(analysisStrategy);
    }

    /**
     * setSystemInfo
     * @param osName
     * @param osArch
     */
    protected synchronized static void setSystemInfo(String osName, String osArch) {
        getReportsInstance.setSystemInfo(osName, osArch);
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
            getReportsInstance
                    .attachReporter(sparkReporter
                    .filter()
                    .statusFilter()
                    .as(new Status[]{status})
                    .apply());
        }
    }

    /**
     * log
     * @param status
     * @param media
     */
    public synchronized static void log(Status status, Media media) {
        extentTest.get().log(status, media);
    }

    /**
     * log
     *
     * @param status
     * @param details
     * @return
     */
    public synchronized static void log(Status status, String details) {
        extentTest.get().log(status, details);
    }

    /**
     * log
     * @param status
     * @param markup
     */
    public synchronized static void log(Status status, Markup markup) {
        extentTest.get().log(status, markup);
    }

    /**
     * log
     * @param status
     * @param throwable
     */
    public synchronized static void log(Status status, Throwable throwable) {
        extentTest.get().log(status, throwable);
    }

    /**
     * log
     * @param status
     * @param details
     * @param media
     */
    public synchronized static void log(Status status, String details, Media media) {
        extentTest.get().log(status, details, media);
    }

    /**
     * log
     * @param status
     * @param throwable
     * @param media
     */
    public synchronized static void log(Status status, Throwable throwable, Media media) {
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
        extentTest.get().log(status, details, throwable, media);
    }

    public synchronized static void logJson(Status status, String nodeDesc, String json) {
        extentTest.get().createNode(nodeDesc).log(status, MarkupHelper.createCodeBlock(json, CodeLanguage.JSON));
    }

    public synchronized static void logTable(Status status, String nodeDesc, List<Header> list) {
        String[][] array = list.stream().map(header -> new String[] {header.getName(), header.getValue()}).toArray(String[][] :: new);
        extentTest.get().createNode(nodeDesc).log(status, MarkupHelper.createTable(array));
    }

    public synchronized static void logTable(Status status, String nodeDesc, Map<String,String> map) {
        String[][] array = map.entrySet().stream().map(e -> new String[] { e.getKey(), e.getValue() }).toArray(String[][]::new);
        extentTest.get().createNode(nodeDesc).log(status, MarkupHelper.createTable(array));
    }

    public synchronized static void logCodeBlock(Status status, String nodeDesc, String message) {
        extentTest.get().createNode(nodeDesc).log(status, MarkupHelper.createCodeBlock(message));
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
                Media media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64ScreenShot).build();
                if (createNode) extentTest.get().createNode("click for more details... ").log(status, message, media);
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
    public synchronized static void onFail(FailStatus status, String expendMessage, String bodyDesc) {
        try {
            extentTest.get().createNode("test error, click for more details... ")
                    .log(status.getStatus(), expendMessage)
                    .log(status.getStatus(), bodyDesc);
        } catch (Exception ignore) {}
    }

    public synchronized static <T> void onFail(FailStatus status, String expendMessage, List<T> bodyDesc) {
        try {
            extentTest.get()
                    .createNode("test error, click for more details... ")
                    .log(status.getStatus(), expendMessage)
                    .log(status.getStatus(), MarkupHelper.createOrderedList(bodyDesc));
        } catch (Exception ignore) {}
    }

    protected synchronized static List<Log> getExtentLogs() {
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
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
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

}
