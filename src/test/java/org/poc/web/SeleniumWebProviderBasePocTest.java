package org.poc.web;

import com.aventstack.extentreports.Status;
import org.automation.base.elements.PageFactoryGenerator;
import org.automation.base.web.SeleniumWebDriverManager;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.DriverEventListener;
import org.extensions.automation.web.WebDriverProviderExtension;
import org.extensions.web.DriverType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import static java.time.Duration.ofSeconds;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WebDriverProviderExtension.class)
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { Status.FAIL, Status.SKIP }, reportSettingsPath = "project.report.config")
public class SeleniumWebProviderBasePocTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @WebDriverType(driversInstance = { ChromeDriver.class, FirefoxDriver.class}, durationTimeOuts = 30)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    void openPageFirstTest(@Autowired SeleniumWebDriverManager webDriverExtension) {
        webDriverExtension.get("https://bonigarcia.dev/selenium-webdriver-java");
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = PageFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(ExpectedConditions.elementToBeClickable(boniGarciaPage.link));
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @WebDriverType(driversInstance = { ChromeDriver.class, FirefoxDriver.class}, durationTimeOuts = 30)
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    void openPageSecondTest(@Autowired SeleniumWebDriverManager webDriverExtension) {
        webDriverExtension.get("https://bonigarcia.dev/selenium-webdriver-java");
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = PageFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(ExpectedConditions.elementToBeClickable(boniGarciaPage.link));
    }
}
