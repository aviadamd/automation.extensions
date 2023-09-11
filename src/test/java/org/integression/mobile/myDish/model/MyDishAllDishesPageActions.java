package org.integression.mobile.myDish.model;

import org.base.mobile.MobileDriverProvider;
import org.base.mobile.ScrollDirection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.utils.assertions.AssertionsManager;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class MyDishAllDishesPageActions {

    private final AssertionsManager assertionsManager;
    private final MobileDriverProvider mobileDriverProvider;

    public MyDishAllDishesPageActions(MobileDriverProvider mobileDriverProvider, AssertionsManager assertionsManager) {
        this.mobileDriverProvider = mobileDriverProvider;
        this.assertionsManager = assertionsManager;
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
