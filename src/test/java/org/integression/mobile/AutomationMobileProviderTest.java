package org.integression.mobile;

import com.aventstack.extentreports.AnalysisStrategy;
import lombok.extern.slf4j.Slf4j;
import org.data.files.jsonReader.JacksonExtension;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.mobile.appium.Android;
import org.extensions.anontations.mobile.appium.CapabilitiesInjections;
import org.extensions.anontations.mobile.appium.Ios;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.automation.mobile.CapabilitiesObject;
import org.extensions.automation.mobile.MobileSharedObjects;
import org.extensions.automation.mobile.MobileSharedObjectsProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import java.io.File;
import java.util.List;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, MobileSharedObjectsProviderExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST,
        mongoConnection = "project.mongo.connection"
)
public class AutomationMobileProviderTest {

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @DriverProvider(proxyPort = 0, jsonCapsPath = "android.caps.json")
    @CapabilitiesInjections(android = @Android(keys = {""}, values = {""}), ios = @Ios(keys = {""}, values = {""}))
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void a_mobileTest(MobileSharedObjects<CapabilitiesObject> mobileSharedObjects) {
        String path = System.getProperty("user.dir") + "/" + "src/test/resources/androidCaps1.json";
        mobileSharedObjects.setJacksonExtension(new JacksonExtension<>(path, new File(path), CapabilitiesObject.class));
        List<CapabilitiesObject> capabilitiesObjects = mobileSharedObjects.getJacksonExtension().readAllFromJson();
        log.info(capabilitiesObjects.toString());

        final String packageId = "com.ideomobile.hapoalim:id/";
        mobileSharedObjects.getDriverManager().click(elementToBeClickable(By.id(packageId + "login_user_name_view_automation")));
        mobileSharedObjects.getDriverManager().click(elementToBeClickable(By.id(packageId + "login_password_view_automation")));

    }
}
