package org.poc.web;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import org.automation.elements.ObjectFactoryGenerator;
import org.automation.web.SeleniumWebDriverManager;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.web.WebDriverProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.TimeUnit;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, WebDriverProviderExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST
)
public class SeleniumWebProviderBasePocTest {

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @WebDriverType(isWithProxy = true, baseUrl = "project.url", driversInstance = { ChromeDriver.class }, generalTo = 30)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void a_openPageFirstTest(@Autowired SeleniumWebDriverManager webDriverExtension) {
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @WebDriverType(isWithProxy = true, baseUrl = "project.url", driversInstance = { ChromeDriver.class }, generalTo = 30)
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void b_openPageSecondTest(@Autowired SeleniumWebDriverManager webDriverExtension) {
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @WebDriverType(isWithProxy = true, baseUrl = "project.url", driversInstance = { ChromeDriver.class }, generalTo = 30)
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void c_openPageFirstTest(@Autowired SeleniumWebDriverManager webDriverExtension) {
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @WebDriverType(isWithProxy = true, baseUrl = "project.url", driversInstance = { ChromeDriver.class }, generalTo = 30)
    @TestReportInfo(testId = 4, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void d_openPageSecondTest(@Autowired SeleniumWebDriverManager webDriverExtension) {
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(elementToBeClickable(boniGarciaPage.link));
    }
}
