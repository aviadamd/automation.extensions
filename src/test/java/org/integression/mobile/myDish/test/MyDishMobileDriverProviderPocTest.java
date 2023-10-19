package org.integression.mobile.myDish.test;

import com.aventstack.extentreports.AnalysisStrategy;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.assertions.AssertJExtensionProvider;
import org.extensions.mobile.MobileDriverProviderExtension;
import org.extensions.mobile.MobileProvider;
import org.integression.mobile.myDish.viewModel.MyDishAllDishesViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.extensions.mobile.ApplicationLaunchOption.LAUNCH;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = {  MobileDriverProviderExtension.class, AssertJExtensionProvider.class })
@ReportSetUp(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST, repeatOnStatus = { FAIL } )
public class MyDishMobileDriverProviderPocTest {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities1.json", appLaunchOption = LAUNCH, implicitlyWait = 5)
    public void a_lowerBarNavigationTest(MobileProvider mobileProvider) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(mobileProvider);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish(10,"Almost Heaven Cake");
    }

    @Test
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapabilitiesFile = "devicesCapabilities2.json", appLaunchOption = LAUNCH, implicitlyWait = 5)
    public void b_lowerBarNavigationTest(MobileProvider mobileProvider) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(mobileProvider);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish(10, "Almost Heaven Cake");
    }
}
