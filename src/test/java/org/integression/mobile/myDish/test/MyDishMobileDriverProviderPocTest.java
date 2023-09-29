package org.integression.mobile.myDish.test;

import com.aventstack.extentreports.AnalysisStrategy;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.assertions.AssertJExtensionProvider;
import org.extensions.automation.mobile.MobileDriverProviderExtension;
import org.extensions.automation.mobile.MobileProvider;
import org.extensions.report.ExtentReportExtension;
import org.integression.mobile.myDish.viewModel.MyDishAllDishesViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import java.util.concurrent.TimeUnit;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.extensions.automation.mobile.ApplicationLaunchOption.LAUNCH;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, MobileDriverProviderExtension.class, AssertJExtensionProvider.class })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST, repeatOnStatus = { FAIL } )
public class MyDishMobileDriverProviderPocTest {

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities1.json", appLaunchOption = LAUNCH, implicitlyWait = 5)
    public void a_lowerBarNavigationTest(MobileProvider mobileProvider) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(mobileProvider);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish(10,"Almost Heaven Cake");
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities2.json", appLaunchOption = LAUNCH, implicitlyWait = 5)
    public void b_lowerBarNavigationTest(MobileProvider mobileProvider) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(mobileProvider);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish(10, "Almost Heaven Cake");
    }
}
