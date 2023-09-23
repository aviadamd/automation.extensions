package org.integression.mobile.myDish.test;

import com.aventstack.extentreports.AnalysisStrategy;
import org.base.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.assertions.AssertionsExtension;
import org.extensions.automation.mobile.MobileDriverProviderExtension;
import org.extensions.report.ExtentReportExtension;
import org.integression.mobile.myDish.viewModel.MyDishAllDishesViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import java.util.concurrent.TimeUnit;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.extensions.automation.mobile.ApplicationLaunchOption.LAUNCH;

@Execution(ExecutionMode.CONCURRENT)
@DisabledForJreRange(min = JRE.JAVA_11, max = JRE.JAVA_15 )
@ExtendWith(value = { ExtentReportExtension.class, MobileDriverProviderExtension.class, AssertionsExtension.class })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST, repeatOnStatus = { FAIL } )
public class MyDishMobileDriverProviderPocTest {

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities1.json", appLaunchOption = LAUNCH, implicitlyWait = 5)
    public void a_lowerBarNavigationTest(MobileDriverProvider driverManager) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(driverManager);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish(10,"Almost Heaven Cake");
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities2.json", appLaunchOption = LAUNCH, implicitlyWait = 5)
    public void b_lowerBarNavigationTest(MobileDriverProvider driverManager) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(driverManager);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish(10, "Almost Heaven Cake");
    }
}
