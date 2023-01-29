package org.automation.elements;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
public class PageFactoryGenerator {
    public static <T extends PageFactoryGenerator> T instantiateWebPage(WebDriver driver, Class<T> pageClass) {
        try {
            return PageFactory.initElements(driver, pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> void instantiateMobilePage(WebDriver driver, Class<T> pageClass) {
        try {
            PageFactory.initElements(new AppiumFieldDecorator(driver), pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
