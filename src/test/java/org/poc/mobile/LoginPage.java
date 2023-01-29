package org.poc.mobile;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.WithTimeout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.temporal.ChronoUnit;

public class LoginPage {

    @WithTimeout(time = 90, chronoUnit = ChronoUnit.SECONDS)
    @AndroidFindBy(id = "com.ideomobile.hapoalim:id/login_user_name_view_automation")
    public WebElement userNameField;

    @WithTimeout(time = 90, chronoUnit = ChronoUnit.SECONDS)
    @AndroidFindBy(id = "com.ideomobile.hapoalim:id/login_password_view_automation")
    public WebElement userPasswordField;

    @WithTimeout(time = 90, chronoUnit = ChronoUnit.SECONDS)
    @AndroidFindBy(id = "com.ideomobile.hapoalim:id/proceedBtn")
    public WebElement continueButton;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
}
