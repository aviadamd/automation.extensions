package org.poc.mobile;

import com.aventstack.extentreports.Status;
import org.base.mobile.MobileDriverManager.MobileDriverType;
import org.extensions.MobileDriverExtension;
import org.base.mobile.MobileDriverManager;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.mobile.MobileDriverProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { MobileDriverExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        extraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
public class MobileBasePocTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @MobileDriverProvider(driverProvider = {
            @DriverProvider(
                    url = "http://localhost:4723/wd/hub",
                    driverType = MobileDriverType.ANDROID,
                    jsonCapsPath = "src/test/resources/androidCaps1.json"
            ),
            @DriverProvider(
                    url = "http://localhost:4724/wd/hub",
                    driverType = MobileDriverType.ANDROID,
                    jsonCapsPath = "src/test/resources/androidCaps2.json"
            )
    })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void mobileTest(MobileDriverManager manager) {
        String loginEle = "com.ideomobile.hapoalim:id/login_user_name_view_automation";
        manager.oveRideTimeOut(Duration.ofSeconds(20), Duration.ofSeconds(5)).click(elementToBeClickable(By.id(loginEle)));
        manager.sendKeys(ExpectedConditions.elementToBeClickable(By.id(loginEle)),"aviad12345");
    }
}
