package org.component.mobile;

import com.aventstack.extentreports.AnalysisStrategy;
import org.base.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.mobile.MobileDriverProviderExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { MobileDriverProviderExtension.class })
@ReportSetUp(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST)
public class MobileBasePocTest {

    @Test
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities1.json")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void a_mobileTest(MobileDriverProvider driverManager) {
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_user_name_view_automation")));
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_password_view_automation")));
    }

    @Test
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities1.json")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void b_mobileTest(MobileDriverProvider driverManager) {
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_user_name_view_automation")));
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_password_view_automation")));
    }
}
