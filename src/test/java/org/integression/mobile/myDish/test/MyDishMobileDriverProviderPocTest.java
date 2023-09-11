package org.integression.mobile.myDish.test;

import org.base.mobile.MobileDriverProvider;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.automation.mobile.MobileDriverProviderExtension;
import org.integression.mobile.MobileDriverBaseTest;
import org.integression.mobile.myDish.viewModel.MyDishAllDishesViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import static org.extensions.automation.mobile.ApplicationLaunchOption.LAUNCH;

@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { MobileDriverProviderExtension.class })
public class MyDishMobileDriverProviderPocTest extends MobileDriverBaseTest {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapsPath = "android.caps.json", appLaunchOption = LAUNCH)
    public void a_lowerBarNavigationTest(MobileDriverProvider driverManager) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(driverManager);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish("Almost Heaven Cake");
    }

    @Test
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    @DriverProvider(proxyPort = 0, jsonCapsPath = "android.caps.json", appLaunchOption = LAUNCH)
    public void b_lowerBarNavigationTest(MobileDriverProvider driverManager) {
        MyDishAllDishesViewModel myDishAllDishesViewModel = new MyDishAllDishesViewModel(driverManager);
        myDishAllDishesViewModel.lowerBarNavigation();
        myDishAllDishesViewModel.selectSavedDish("Almost Heaven Cake");
    }
}
