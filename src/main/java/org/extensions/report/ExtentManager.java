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

    private static ExtentSparkReporter extentSparkReporter;
    protected synchronized static ExtentReports getReportsInstance() {
        return extentInstance;
    }
    protected synchronized static ExtentSparkReporter getExtentSparkInstance() {
        return extentSparkReporter;
    }
    protected static synchronized ExtentReports createInstance(String file, String jsonSettingsPath, String reportName) {
        try {
            extentInstance = new ExtentReports();
            extentSparkReporter = new ExtentSparkReporter(file);
            extentSparkReporter.viewConfigurer()
                    .viewOrder()
                    .as(new ViewName[] { DASHBOARD, TEST, AUTHOR, DEVICE, EXCEPTION, LOG})
                    .apply();
            extentInstance.attachReporter(extentSparkReporter);
            extentSparkReporter.loadJSONConfig(new File(jsonSettingsPath));
            extentSparkReporter.config().setReportName(reportName);
        } catch (Exception exception) {
            Assertions.fail("ExtentManager createInstance error " + exception.getMessage(), exception);
        }
        return extentInstance;
    }
}



