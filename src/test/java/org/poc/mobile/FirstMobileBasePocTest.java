package org.poc.mobile;

import org.extensions.mobile.MobileDriverExtension;
import org.automation.base.mobile.MobileDriverManager;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { MobileDriverExtension.class })
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { FAIL, SKIP }, reportSettingsPath = "project.report.config")
public class FirstMobileBasePocTest {
    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverProvider(jsonCapsPath = "android.caps.json.first")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void a_mobileTest(MobileDriverManager manager) {
        LoginPage loginPage = new LoginPage(manager.getMobileDriver());
        manager.click(loginPage.userNameField);
        manager.click(loginPage.userPasswordField);
    }
    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverProvider(jsonCapsPath = "android.caps.json.second")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void b_mobileTest(MobileDriverManager manager) {
        LoginPage loginPage = new LoginPage(manager.getMobileDriver());
        manager.click(loginPage.userNameField);
        manager.click(loginPage.userPasswordField);
    }
}
