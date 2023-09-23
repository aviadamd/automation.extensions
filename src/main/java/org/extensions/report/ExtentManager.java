package org.extensions.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import static com.aventstack.extentreports.reporter.configuration.ViewName.*;
import static com.aventstack.extentreports.reporter.configuration.ViewName.LOG;

public class ExtentManager {
    private static ExtentReports extentInstance;
    protected synchronized static ExtentReports getReportsInstance() { return extentInstance; }

    /**
     * setExtentManager
     * @param file
     * @param jsonSettingsPath
     * @param reportName
     */
    protected static synchronized void setExtentManager(String file, String jsonSettingsPath, String reportName) {
        try {
            extentInstance = new ExtentReports();
            ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(file);
            ViewName[] viewNames = { DASHBOARD, TEST, AUTHOR, DEVICE, EXCEPTION, LOG };
            extentSparkReporter.viewConfigurer()
                    .viewOrder()
                    .as(viewNames)
                    .apply();
            extentInstance.attachReporter(extentSparkReporter);
            extentSparkReporter.loadJSONConfig(new File(jsonSettingsPath));
            extentSparkReporter.config().setReportName(reportName);
        } catch (Exception exception) {
            Assertions.fail("ExtentManager createInstance error " + exception.getMessage(), exception);
        }
    }

    protected static synchronized void flush() {
        try {
            extentInstance.flush();
        } catch (Exception exception) {
            Assertions.fail("fail to flush report " + exception.getMessage(), exception);
        }
    }
}



