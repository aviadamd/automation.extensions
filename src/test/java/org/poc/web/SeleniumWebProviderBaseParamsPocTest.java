package org.poc.web;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import org.automation.web.SeleniumWebDriverManager;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.automation.web.WebDriverProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class })
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { FAIL, SKIP }, reportSettingsPath = "project.report.config", analysisStrategy = AnalysisStrategy.TEST, mongoConnection = "project.mongo.connection")
public class SeleniumWebProviderBaseParamsPocTest {

    @ParameterizedTest
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @ValueSource(classes = { ChromeDriver.class, FirefoxDriver.class })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    void test(Class<? extends WebDriver> webDriverClass) {
        SeleniumWebDriverManager webDriverExtension = new SeleniumWebDriverManager("https://bonigarcia.dev/webdrivermanager/", webDriverClass, Duration.ofSeconds(1));

    }
}
