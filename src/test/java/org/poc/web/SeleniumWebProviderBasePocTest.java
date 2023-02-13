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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import java.util.concurrent.TimeUnit;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, WebDriverProviderExtension.class })
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { FAIL, SKIP }, reportSettingsPath = "project.report.config", analysisStrategy = AnalysisStrategy.TEST, mongoConnection = "project.mongo.connection")
public class SeleniumWebProviderBasePocTest {

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "SeleniumWebProviderBasePocTest")
    @WebDriverType(proxyPort = 0, baseUrl = "project.url", driversInstance = "project.client", generalTo = 30)
    void openPageFirstTest(SeleniumWebDriverManager webDriverExtension) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGrciaWelcomePage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.homeTab));
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "SeleniumWebProviderBasePocTest")
    @WebDriverType(proxyPort = 0, baseUrl = "project.url", driversInstance = "project.client", generalTo = 30)
    void openPageSecondTest(SeleniumWebDriverManager webDriverExtension) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGrciaWelcomePage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.homeTab));
    }

    @Test
    @Timeout(value = 3, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "SeleniumWebProviderBasePocTest")
    @WebDriverType(proxyPort = 0, baseUrl = "project.url", driversInstance = "project.client", generalTo = 30)
    void openPageThirdTest(SeleniumWebDriverManager webDriverExtension) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = ObjectFactoryGenerator.instantiateWebPage(webDriverExtension.getDriver(), BoniGrciaWelcomePage.class);
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.homeTab));
    }
}
