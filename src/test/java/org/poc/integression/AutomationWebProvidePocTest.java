package org.poc.integression;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.data.files.jsonReader.JacksonExtension;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.poc.web.BoniGrciaWelcomePageShared;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    private BoniGrciaWelcomePageShared boniGrciaWelcomePageShared;

    @BeforeEach
    public void init(WebSharedObjects<CapabilitiesObject> webSharedObjects) {
        this.boniGrciaWelcomePageShared = new BoniGrciaWelcomePageShared(webSharedObjects.getDriverManager());
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @Timeout(value = 1, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestOne")
    void boniGrciaWelcomePageTestOne(WebSharedObjects<CapabilitiesObject> webSharedObjects) {
        String path = System.getProperty("user.dir") + "/" + "src/test/resources/androidCaps1.json";
        webSharedObjects.setJacksonExtension(new JacksonExtension<>(path, new File(path), CapabilitiesObject.class));
        List<CapabilitiesObject> capabilitiesObjects = webSharedObjects.getJacksonExtension().readAllFromJson();
        log.info(capabilitiesObjects.toString());

        this.boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink()
                .openPage(webSharedObjects.getWebConfiguration());
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @Timeout(value = 1, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestTwo")
    void boniGrciaWelcomePageTestTwo(WebSharedObjects<?> webSharedObjects) {
        this.boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @Timeout(value = 1, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "boniGrciaWelcomePageTestThree")
    void boniGrciaWelcomePageTestThree(WebSharedObjects<?> webSharedObjects) {
        this.boniGrciaWelcomePageShared.resumeTab()
                .homeTab()
                .gitHubLink()
                .openPage(webSharedObjects.getWebConfiguration())
                .linkedinLink();
    }
}
