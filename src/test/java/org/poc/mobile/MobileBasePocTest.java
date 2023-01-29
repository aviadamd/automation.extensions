package org.poc.mobile;

import org.base.mobile.MobileDriverExtension;
import org.base.mobile.MobileDriverManager;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mobile.elements.PageFactoryGenerator;
import java.time.Duration;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(value = Lifecycle.PER_METHOD)
@ExtendWith(value = { MobileDriverExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
public class MobileBasePocTest {
    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverProvider(dirPath = "user.dir", jsonCapsPath = "\\src\\test\\resources\\androidCaps1.json")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void a_mobileTest(MobileDriverManager manager) {
        LoginPage loginPage = new PageFactoryGenerator().init(manager.getMobileDriver(), LoginPage.class);
        manager.oveRideTimeOut(Duration.ofSeconds(20), Duration.ofSeconds(5)).click(elementToBeClickable(loginPage.onBoarding));
        manager.sendKeys(elementToBeClickable(loginPage.userNameField),"aviad12345");
        manager.sendKeys(elementToBeClickable(loginPage.userPasswordField),"ss123456");
        manager.oveRideTimeOut(Duration.ofSeconds(5), Duration.ofSeconds(1)).click(elementToBeClickable(loginPage.continueButton));
    }
    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverProvider(dirPath = "user.dir", jsonCapsPath = "\\src\\test\\resources\\androidCaps2.json")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void b_mobileTest(MobileDriverManager manager) {
        LoginPage loginPage = new PageFactoryGenerator().init(manager.getMobileDriver(), LoginPage.class);
        manager.oveRideTimeOut(Duration.ofSeconds(20), Duration.ofSeconds(5)).click(elementToBeClickable(loginPage.onBoarding));
        manager.sendKeys(elementToBeClickable(loginPage.userNameField),"aviad12345");
        manager.sendKeys(elementToBeClickable(loginPage.userPasswordField),"ss123456");
        manager.oveRideTimeOut(Duration.ofSeconds(5), Duration.ofSeconds(1)).click(elementToBeClickable(loginPage.continueButton));
    }
}
