package org.poc.integression;


import com.aventstack.extentreports.AnalysisStrategy;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.mobile.appium.AndroidServerArgumentsInjections;
import org.extensions.anontations.mobile.appium.AppiumServerArgumentsInjections;
import org.extensions.anontations.mobile.appium.IosServerArgumentsInjections;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.automation.mobile.MobileSharedObjects;
import org.extensions.automation.mobile.MobileSharedObjectsProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, MobileSharedObjectsProviderExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST,
        mongoConnection = "project.mongo.connection"
)
public class MobileProviderTest {

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverProvider(proxyPort = 0, jsonCapsPath = "android.caps.json")
    @AppiumServerArgumentsInjections(
            android = @AndroidServerArgumentsInjections(keys = {""}, values = {""}),
            ios = @IosServerArgumentsInjections(keys = {""}, values = {""})
    )
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void a_mobileTest(MobileSharedObjects provider) {
        provider.getDriverManager().click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_user_name_view_automation")));
        provider.getDriverManager().click(elementToBeClickable(By.id("com.ideomobile.hapoalim:id/login_password_view_automation")));

    }
}
