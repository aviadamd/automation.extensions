package org.poc.web;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import org.automation.web.SeleniumWebDriverManager;
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
import org.automation.elements.ObjectFactoryGenerator;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static java.time.Duration.ofSeconds;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST,
        mongoConnection = "mongodb://localhost:27017"
)
public class SeleniumWebBasePocTest {

    @ParameterizedTest
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @ValueSource(classes = { ChromeDriver.class, ChromeDriver.class })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void a_openPageFirstTest(Class<? extends SeleniumWebDriverManager> webDriverClass) {
        SeleniumWebDriverManager webDriverExtension = new SeleniumWebDriverManager(webDriverClass, ofSeconds(10));
        webDriverExtension.get(System.getProperty("project.url"));
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(ExpectedConditions.elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
    }

    @ParameterizedTest
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @ValueSource(classes = { ChromeDriver.class })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void b_openPageFirstTest(Class<? extends SeleniumWebDriverManager> webDriverClass) {
        SeleniumWebDriverManager webDriverExtension = new SeleniumWebDriverManager(webDriverClass, ofSeconds(10));
        webDriverExtension.get(System.getProperty("project.url"));
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(ExpectedConditions.elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
    }

    @ParameterizedTest
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @ValueSource(classes = { ChromeDriver.class })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void c_openPageFirstTest(Class<? extends SeleniumWebDriverManager> webDriverClass) {
        SeleniumWebDriverManager webDriverExtension = new SeleniumWebDriverManager(webDriverClass, ofSeconds(10));
        webDriverExtension.get(System.getProperty("project.url"));
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(ExpectedConditions.elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
    }

    @ParameterizedTest
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @ValueSource(classes = { ChromeDriver.class })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void d_openPageFirstTest(Class<? extends SeleniumWebDriverManager> webDriverClass) {
        SeleniumWebDriverManager webDriverExtension = new SeleniumWebDriverManager(webDriverClass, ofSeconds(10));
        webDriverExtension.get(System.getProperty("project.url"));
        String title = webDriverExtension.oveRideTimeOut(ofSeconds(10), ofSeconds(2)).getTitle();
        Assertions.assertTrue(title.contains("Selenium WebDriver"));
        BoniGarciaPage boniGarciaPage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGarciaPage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(5), ofSeconds(2)).click(ExpectedConditions.elementToBeClickable(boniGarciaPage.link));
        webDriverExtension.get(System.getProperty("project.url"));
    }
}
