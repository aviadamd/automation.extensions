package org.integression.mobile.myDish.view;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.base.mobile.InitElementsBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MyDishAllDishesPage extends InitElementsBasePage {

    private final String APP_PACKAGE_ID = "com.example.mydish:id/";
    @AndroidFindBy(id = APP_PACKAGE_ID + "navigation_favorite_dishes")
    public WebElement navigation_favorite_dishes;
    @AndroidFindBy(id = APP_PACKAGE_ID + "navigation_random_dish")
    public WebElement navigation_random_dish;
    @AndroidFindBy(id = APP_PACKAGE_ID + "navigation_all_dishes")
    public WebElement navigation_all_dishes;

    @AndroidFindBy(id = APP_PACKAGE_ID + "rv_dishes_list")
    public WebElement rv_dishes_list;

    public MyDishAllDishesPage(WebDriver driver) {
        super(driver);
    }
}
