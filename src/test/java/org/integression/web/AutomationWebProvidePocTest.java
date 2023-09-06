package org.integression.web;

import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.ProviderConfiguration;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.web.WebSharedObjects;
import org.extensions.automation.web.WebSharedObjectsProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.component.web.BoniGrciaWelcomePageShared;
import org.utils.assertions.AssertionsLevel;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, WebSharedObjectsProviderExtension.class })
@ReportConfiguration
@ProviderConfiguration(
        dbProvider = @MongoMorphiaConnector(host = "project.db.url", dbName = "dbNew"),
        driverProvider = @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", generalTo = 30))
public class AutomationWebProvidePocTest {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestOne")
    void boniGrciaWelcomePageTestOne(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaShared = new BoniGrciaWelcomePageShared(webSharedObjects.getDriverManager());

        boniGrciaShared.setAssertionLevel(AssertionsLevel.HARD_AFTER_ERROR);
        boniGrciaShared.assertHomeTab().isEqualTo("Home");

        boniGrciaShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink()
                .openPage(webSharedObjects.getWebConfiguration());
    }

    @Test
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestTwo")
    void boniGrciaWelcomePageTestTwo(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects.getDriverManager());
        boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
    }

    @Test
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestThree")
    void boniGrciaWelcomePageTestThree(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects.getDriverManager());
        boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
    }
}
