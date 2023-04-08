package org.poc.integression;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.ProviderConfiguration;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.mobile.CapabilitiesObject;
import org.extensions.automation.web.WebSharedObjects;
import org.extensions.automation.web.WebSharedObjectsProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.poc.web.BoniGrciaWelcomePageShared;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, WebSharedObjectsProviderExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST,
        mongoConnection = "project.mongo.connection")
@ProviderConfiguration(
        dbProvider = @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "dbNew"),
        driverProvider = @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", generalTo = 30))
public class AutomationWebProvidePocTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestOne")
    void boniGrciaWelcomePageTestOne(WebSharedObjects<CapabilitiesObject> webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects.getDriverManager());
        boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink()
                .openPage(webSharedObjects.getWebConfiguration());
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestTwo")
    void boniGrciaWelcomePageTestTwo(WebSharedObjects<?> webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects.getDriverManager());
        boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestThree")
    void boniGrciaWelcomePageTestThree(WebSharedObjects<?> webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects.getDriverManager());
        boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
    }
}
