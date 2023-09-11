package org.integression.mobile.myDish.viewModel;

import org.base.mobile.MobileDriverProvider;
import org.base.mobile.gestures.ScrollDirection;
import org.integression.mobile.myDish.view.MyDishAllDishesView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.utils.assertions.AssertionsManager;
import java.util.List;
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

    public void selectSavedDish(String findBy) {
        boolean find = false;
        String text;

        List<WebElement> elements = mobileDriverProvider.findElements(By.id("com.example.mydish:id/dish_view"));
        for (WebElement element: elements) {
            text = mobileDriverProvider.findElement(element, By.className("android.widget.TextView")).getText();
            if (text.equalsIgnoreCase(findBy)) {
                mobileDriverProvider.click(elementToBeClickable(By.className("android.widget.ImageView")));
                find = true;
                break;
            }
            mobileDriverProvider.getSwipeExtensions().swipe(ScrollDirection.DOWN);
        }
        assertionsManager.assertThat(find).isTrue();
    }


}
