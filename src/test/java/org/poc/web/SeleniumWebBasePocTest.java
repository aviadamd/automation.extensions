package org.poc.web;

import com.aventstack.extentreports.Status;
import org.base.web.SeleniumWebDriverManager;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.extensions.DriverEventListener;
import org.mobile.elements.PageFactoryGenerator;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static java.time.Duration.ofSeconds;
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class })
@ReportConfiguration(reportPath = "target/reports",
        extraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
public class SeleniumWebBasePocTest {

    @ParameterizedTest
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @ValueSource(classes = { ChromeDriver.class, FirefoxDriver.class })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    void openPageFirstTest(Class<? extends SeleniumWebDriverManager> webDriverClass) {
        SeleniumWebDriverManager webDriverExtension = new SeleniumWebDriverManager(webDriverClass, new DriverEventListener(), ofSeconds(10));
        webDriverExtension.get("https://bonigarcia.dev/selenium-webdriver-java");
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = new PageFactoryGenerator().init(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(ExpectedConditions.elementToBeClickable(boniGarciaPage.link));
    }
}
