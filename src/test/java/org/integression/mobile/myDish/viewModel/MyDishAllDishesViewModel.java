package org.integression.mobile.myDish.viewModel;

import org.base.mobile.MobileDriverProvider;
import org.base.mobile.gestures.ScrollDirection;
import org.integression.mobile.myDish.view.MyDishAllDishesView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.utils.assertions.AssertionsManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class MyDishAllDishesViewModel {

    private final AssertionsManager assertionsManager;
    private final MobileDriverProvider mobileDriverProvider;
    private final MyDishAllDishesView myDishAllDishesView;

    public MyDishAllDishesViewModel(MobileDriverProvider mobileDriverProvider) {
        this.mobileDriverProvider = mobileDriverProvider;
        this.assertionsManager = new AssertionsManager();
        this.myDishAllDishesView = new MyDishAllDishesView(mobileDriverProvider.getMobileDriver());
    }

    public void lowerBarNavigation() {
        mobileDriverProvider.click(myDishAllDishesView.navigation_random_dish);
        mobileDriverProvider.click(myDishAllDishesView.navigation_favorite_dishes);
        mobileDriverProvider.click(myDishAllDishesView.navigation_all_dishes);
    }

    public void selectSavedDish(int search, String findBy) {
        String text;
        boolean find = false;

        for (int retry = 1; retry < search; retry++) {

            for (WebElement element: this.getAllDishes()) {
                text = mobileDriverProvider.getText(mobileDriverProvider.findElement(element, By.className("android.widget.TextView")));
                if (text.equalsIgnoreCase(findBy)) {
                    mobileDriverProvider.click(elementToBeClickable(By.className("android.widget.ImageView")));
                    find = true;
                    break;
                }
            }

            if (find) break;
            mobileDriverProvider.getSwipeExtensions().swipe(ScrollDirection.DOWN);
        }
        assertionsManager.assertThat(find).describedAs("search saved dish by: " + findBy).isTrue();
    }

    private List<WebElement> getAllDishes() {
        return mobileDriverProvider
                .findElements(By.id(MyDishAllDishesView.APP_PACKAGE_ID + "dish_view"));
    }

}
