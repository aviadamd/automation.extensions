package org.poc.mobile;

import org.automation.base.mobile.MobileDriverManager;
import org.extensions.mobile.CapabilitiesParameterResolver;
import org.extensions.mobile.CapsReaderAdapter;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { CapabilitiesParameterResolver.class })
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { FAIL, SKIP }, reportSettingsPath = "project.report.config")
public class SecondMobileBasePocTest {

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void a_mobileTest(CapsReaderAdapter capsReaderAdapter) {
        MobileDriverManager manager = new MobileDriverManager(capsReaderAdapter.read("android.caps.json.first"));
        LoginPage loginPage = new LoginPage(manager.getMobileDriver());
        manager.click(loginPage.userNameField);
        manager.click(loginPage.userPasswordField);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void b_mobileTest(CapsReaderAdapter capsReaderAdapter) {
        MobileDriverManager manager = new MobileDriverManager(capsReaderAdapter.read("android.caps.json.second"));
        LoginPage loginPage = new LoginPage(manager.getMobileDriver());
        manager.click(loginPage.userNameField);
        manager.click(loginPage.userPasswordField);
    }
}
