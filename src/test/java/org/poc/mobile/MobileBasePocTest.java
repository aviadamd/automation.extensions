package org.poc.mobile;

import com.aventstack.extentreports.AnalysisStrategy;
import org.automation.mobile.MobileDriverManager;
import org.extensions.anontations.mobile.DriverJsonProvider;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.automation.mobile.MobileDriverProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, MobileDriverProviderExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST
)
public class MobileBasePocTest {

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverJsonProvider(jsonCapsPath = "android.caps.json.first")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void a_mobileTest(@Autowired MobileDriverManager driverManager) {
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_user_name_view_automation")));
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_password_view_automation")));
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverJsonProvider(jsonCapsPath = "android.caps.json.second")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void b_mobileTest(@Autowired MobileDriverManager driverManager) {
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_user_name_view_automation")));
        driverManager.click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_password_view_automation")));
    }
}
