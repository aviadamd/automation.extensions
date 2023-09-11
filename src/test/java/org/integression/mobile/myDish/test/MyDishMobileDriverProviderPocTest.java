package org.integression.mobile.myDish.test;

import org.base.mobile.MobileDriverProvider;
import org.base.mobile.ScrollDirection;
import org.extensions.anontations.mobile.DriverProvider;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.automation.mobile.MobileDriverProviderExtension;
import org.integression.mobile.MobileDriverBaseTest;
import org.integression.mobile.myDish.view.MyDishAllDishesPage;
import org.integression.mobile.myDish.model.MyDishAllDishesPageActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.utils.assertions.AssertionsManager;


@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { MobileDriverProviderExtension.class })
public class MyDishMobileDriverProviderPocTest extends MobileDriverBaseTest {

    @Test
    @DriverProvider(proxyPort = 0, jsonCapsPath = "android.caps.json")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void a_lowerBarNavigationTest(MobileDriverProvider driverManager) {
        MyDishAllDishesPage myDishAllDishesPage = new MyDishAllDishesPage(driverManager.getMobileDriver());
        driverManager.click(myDishAllDishesPage.navigation_random_dish);
        driverManager.click(myDishAllDishesPage.navigation_favorite_dishes);
        driverManager.click(myDishAllDishesPage.navigation_all_dishes);
        MyDishAllDishesPageActions myDishAllDishesPageActions = new MyDishAllDishesPageActions(driverManager, new AssertionsManager());
        myDishAllDishesPageActions.selectSavedDish("Almost Heaven Cake");
        driverManager.getAndroidDriver().closeApp();
    }

    @Test
    @DriverProvider(proxyPort = 0, jsonCapsPath = "android.caps.json")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MobileBasePocTest")
    public void b_lowerBarNavigationTest(MobileDriverProvider driverManager) {
        MyDishAllDishesPage myDishAllDishesPage = new MyDishAllDishesPage(driverManager.getMobileDriver());
        driverManager.click(myDishAllDishesPage.navigation_random_dish);
        driverManager.click(myDishAllDishesPage.navigation_favorite_dishes);
        driverManager.click(myDishAllDishesPage.navigation_all_dishes);
        MyDishAllDishesPageActions myDishAllDishesPageActions = new MyDishAllDishesPageActions(driverManager, new AssertionsManager());
        myDishAllDishesPageActions.selectSavedDish("Almost Heaven Cake");
        driverManager.getAndroidDriver().closeApp();
    }
}
