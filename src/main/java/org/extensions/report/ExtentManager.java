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
    private static ExtentManager m_instance;

    protected static ExtentManager getInstance() {
        if (m_instance == null) m_instance = new ExtentManager();
        return m_instance;
    }

    protected synchronized ExtentReports extentReportInstance() {
        if (extentInstance == null) extentInstance = new ExtentReports();
        return extentInstance;
    }

    /**
     * setExtentManager
     * @param file
     * @param jsonSettingsPath
     * @param reportName
     */
    protected synchronized void setExtentManager(String file, String jsonSettingsPath, String reportName) {
        try {

            ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(file);
            ViewName[] viewNames = { DASHBOARD, TEST, AUTHOR, DEVICE, EXCEPTION, LOG };
            extentSparkReporter.viewConfigurer()
                    .viewOrder()
                    .as(viewNames)
                    .apply();
            extentReportInstance().attachReporter(extentSparkReporter);
            extentSparkReporter.loadJSONConfig(new File(jsonSettingsPath));
            extentSparkReporter.config().setReportName(reportName);

        } catch (Exception exception) {
            Assertions.fail("ExtentManager createInstance error " + exception.getMessage(), exception);
        }
    }

    protected synchronized void flush() {
        try {
            extentReportInstance().flush();
        } catch (Exception exception) {
            Assertions.fail("fail to flush report " + exception.getMessage(), exception);
        }
    }
}



