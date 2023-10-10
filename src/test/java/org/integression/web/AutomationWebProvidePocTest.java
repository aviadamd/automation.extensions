package org.integression.web;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.WebProviderConfiguration;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.web.WebSharedObjects;
import org.extensions.web.WebSharedObjectsProviderExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.component.web.BoniGrciaWelcomePageShared;

@Slf4j
@ReportSetUp
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = WebSharedObjectsProviderExtension.class)
public class AutomationWebProvidePocTest {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestOne")
    @WebProviderConfiguration(driverProvider = @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", fluentWaitGeneralTo = 30))
    void boniGrciaWelcomePageTestOne(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaShared = new BoniGrciaWelcomePageShared(webSharedObjects);

        boniGrciaShared.assertHomeTab("Home");

        boniGrciaShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink()
                .openPage(webSharedObjects.getWebConfiguration());

        webSharedObjects.extentManager().log(Status.INFO,"bla bla");
    }

    @Test
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestTwo")
    @WebProviderConfiguration(driverProvider = @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", fluentWaitGeneralTo = 30))
    void boniGrciaWelcomePageTestTwo(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects);
        boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
        webSharedObjects.extentManager().log(Status.INFO,"bla bla");
    }

    @Test
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestThree")
    @WebProviderConfiguration(driverProvider = @WebDriverType(baseUrl = "project.url", driversInstance = "project.client", fluentWaitGeneralTo = 30))
    void boniGrciaWelcomePageTestThree(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePageShared boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects);
        boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
        webSharedObjects.extentManager().log(Status.INFO,"bla bla");
    }
}
