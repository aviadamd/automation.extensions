package org.integression.mobile.myDish.viewModel;

import org.assertj.core.api.BooleanAssert;
import org.base.mobile.gestures.ScrollDirection;
import org.extensions.automation.mobile.MobileProvider;
import org.integression.mobile.myDish.view.MyDishAllDishesView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class MyDishAllDishesViewModel {

    private final MobileProvider mobileProvider;
    private final MyDishAllDishesView myDishAllDishesView;

    public MyDishAllDishesViewModel(MobileProvider mobileProvider) {
        this.mobileProvider = mobileProvider;
        this.myDishAllDishesView = new MyDishAllDishesView(mobileProvider.getDriverManager().getMobileDriver());
    }

    public void lowerBarNavigation() {
        mobileProvider.getDriverManager().click(myDishAllDishesView.navigation_random_dish);
        mobileProvider.getDriverManager().click(myDishAllDishesView.navigation_favorite_dishes);
        mobileProvider.getDriverManager().click(myDishAllDishesView.navigation_all_dishes);
    }

    public void selectSavedDish(int search, String findBy) {
        String text;
        boolean find = false;

        for (int retry = 1; retry < search; retry++) {

            for (WebElement element: this.getAllDishes()) {
                text = mobileProvider.getDriverManager().getText(
                        mobileProvider.getDriverManager().findElement(element, By.className("android.widget.TextView")));
                if (text.equalsIgnoreCase(findBy)) {
                    mobileProvider.getDriverManager().click(elementToBeClickable(By.className("android.widget.ImageView")));
                    find = true;
                    break;
                }
            }

            if (find) break;
            mobileProvider.getDriverManager().getSwipeExtensions().swipe(ScrollDirection.DOWN);
        }
        mobileProvider.getAssertionsManager().proxy(BooleanAssert.class, Boolean.class, find).isTrue();
    }

    private List<WebElement> getAllDishes() {
        return mobileProvider
                .getDriverManager()
                .findElements(By.id(MyDishAllDishesView.APP_PACKAGE_ID + "dish_view"));
    }

}
