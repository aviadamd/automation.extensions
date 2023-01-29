package org.poc.mobile;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.mobile.elements.PageFactoryGenerator;
import org.openqa.selenium.WebElement;

public class LoginPage extends PageFactoryGenerator {
    private final String ANDROID_PACKAGE = "com.ideomobile.hapoalim:id/";
    @iOSXCUITFindBy(id = "LoginComponent_userNameTestField")
    @AndroidFindBy(id = ANDROID_PACKAGE + "login_user_name_view_automation")
    public WebElement userNameField;
    @iOSXCUITFindBy(id = "LoginComponent_passwordTestField")
    @AndroidFindBy(id = ANDROID_PACKAGE + "login_password_view_automation")
    public WebElement userPasswordField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@text='בפעם אחרת']")
    @iOSXCUITFindBy(id = "בפעם אחרת")
    public WebElement onBoarding;

    @iOSXCUITFindBy(id = "LoaderLottieButton_animationViewContainer")
    @AndroidFindBy(id = ANDROID_PACKAGE + "proceedBtn")
    public WebElement continueButton;

}
