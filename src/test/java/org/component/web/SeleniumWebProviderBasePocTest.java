package org.component.web;

import com.aventstack.extentreports.AnalysisStrategy;
import org.base.web.DurationOf;
import org.base.web.SeleniumWebDriverProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.web.WebDriverProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, WebDriverProviderExtension.class })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST)
public class SeleniumWebProviderBasePocTest {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "SeleniumWebProviderBasePocTest")
    @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", generalTo = 30, durationOf = DurationOf.MINUTES)
    void openPageFirstTest(SeleniumWebDriverProvider webDriverExtension) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = new BoniGrciaWelcomePage(webDriverExtension.getDriver());
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.homeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
    }

    @Test
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "SeleniumWebProviderBasePocTest")
    @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", generalTo = 30)
    void openPageSecondTest(SeleniumWebDriverProvider webDriverExtension) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = new BoniGrciaWelcomePage(webDriverExtension.getDriver());
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.homeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
    }

    @Test
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "SeleniumWebProviderBasePocTest")
    @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", generalTo = 30)
    void openPageThirdTest(SeleniumWebDriverProvider webDriverExtension) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = new BoniGrciaWelcomePage(webDriverExtension.getDriver());
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.homeTab));
        webDriverExtension.oveRideTimeOut(ofSeconds(15), ofSeconds(2)).click(elementToBeClickable(boniGrciaWelcomePage.resumeTab));
    }
}
