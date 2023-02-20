package org.poc.web;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.JacksonProvider;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.ProviderConfiguration;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.web.WebDriverType;
import org.extensions.automation.web.WebSharedObjects;
import org.extensions.automation.web.WebSharedObjectsProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.poc.jackson.ObjectPojo;
import java.util.concurrent.TimeUnit;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, WebSharedObjectsProviderExtension.class })
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { FAIL, SKIP }, reportSettingsPath = "project.report.config", analysisStrategy = AnalysisStrategy.TEST, mongoConnection = "project.mongo.connection")
public class  AutomationWebProvidePocTest {

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "openPageFirstTest")
    @ProviderConfiguration(
            jacksonProvider = @JacksonProvider(dir = "src/test/resources", fileName = "someJson.json", classObject = ObjectPojo.class),
            driverProvider = @WebDriverType(proxyPort = 0, baseUrl = "project.url", driversInstance = "project.client", generalTo = 30),
            dbProvider = @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "dbNew"))
    void openPageFirstTest(WebSharedObjects webSharedObjects) {
        webSharedObjects.log(Status.INFO,"bla bla");
        BoniGrciaWelcomePage boniGrciaWelcomePage = new BoniGrciaWelcomePage(webSharedObjects.getDriverManager().getDriver());
        webSharedObjects.getDriverManager().click(boniGrciaWelcomePage.resumeTab);
        webSharedObjects.getDriverManager().click(boniGrciaWelcomePage.homeTab);
        log.info(webSharedObjects.getWebConfiguration().projectClient());
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "openPageSecondTest")
    @ProviderConfiguration(
            jacksonProvider = @JacksonProvider(dir = "src/test/resources", fileName = "someJson.json", classObject = ObjectPojo.class),
            driverProvider = @WebDriverType(proxyPort = 0, baseUrl = "project.url", driversInstance = "project.client", generalTo = 30),
            dbProvider = @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "dbNew"))
    void openPageSecondTest(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = new BoniGrciaWelcomePage(webSharedObjects.getDriverManager().getDriver());
        webSharedObjects.getDriverManager().click(boniGrciaWelcomePage.resumeTab);
        webSharedObjects.getDriverManager().click(boniGrciaWelcomePage.homeTab);
        log.info(webSharedObjects.getWebConfiguration().projectClient());
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES)
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "openPageThirdTest")
    @ProviderConfiguration(
            jacksonProvider = @JacksonProvider(dir = "src/test/resources", fileName = "someJson.json", classObject = ObjectPojo.class),
            driverProvider = @WebDriverType(proxyPort = 0, baseUrl = "project.url", driversInstance = "project.client", generalTo = 30),
            dbProvider = @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "dbNew"))
    void openPageThirdTest(WebSharedObjects webSharedObjects) {
        BoniGrciaWelcomePage boniGrciaWelcomePage = new BoniGrciaWelcomePage(webSharedObjects.getDriverManager().getDriver());
        webSharedObjects.getDriverManager().click(boniGrciaWelcomePage.resumeTab);
        webSharedObjects.getDriverManager().click(boniGrciaWelcomePage.homeTab);
        log.info(webSharedObjects.getWebConfiguration().projectClient());
    }
}
