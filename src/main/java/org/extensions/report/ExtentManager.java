package org.extensions.report;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import static com.aventstack.extentreports.reporter.configuration.ViewName.*;
import static com.aventstack.extentreports.reporter.configuration.ViewName.LOG;

public class ExtentManager {
    private static ExtentReports extentInstance;
    protected synchronized static ExtentReports getReportsInstance() {
        return extentInstance;
    }
    protected static synchronized ExtentReports createInstance(String fileName, String jsonSettingsPath) {
        try {
            extentInstance = new ExtentReports();
            ExtentSparkReporter sparkReporterInstance = new ExtentSparkReporter(fileName);
            sparkReporterInstance.viewConfigurer()
                    .viewOrder()
                    .as(new ViewName[] { DASHBOARD, TEST, AUTHOR, DEVICE, EXCEPTION, LOG})
                    .apply();
            extentInstance.attachReporter(sparkReporterInstance);
            sparkReporterInstance.loadJSONConfig(new File(jsonSettingsPath));
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
        return extentInstance;
    }
}



