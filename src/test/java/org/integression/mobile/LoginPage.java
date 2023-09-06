package org.integression.mobile;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @AndroidFindBy(id = "com.ideomobile.hapoalim:id/login_user_name_view_automation")
    public WebElement userNameField;

    @AndroidFindBy(id = "com.ideomobile.hapoalim:id/login_password_view_automation")
    public WebElement userPasswordField;

    @AndroidFindBy(id = "com.ideomobile.hapoalim:id/proceedBtn")
    public WebElement continueButton;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
}
