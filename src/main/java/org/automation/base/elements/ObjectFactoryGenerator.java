package org.automation.base.elements;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
public class ObjectFactoryGenerator extends PageFactory {
    public static <T extends ObjectFactoryGenerator> T instantiateWebPage(WebDriver driver, Class<T> pageClass) {
        try {
            return PageFactory.initElements(driver, pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    @Deprecated
    public static <T extends ObjectFactoryGenerator> T instantiateMobilePage(WebDriver driver, Class<T> pageClass) {
        try {
            PageFactory.initElements(new AppiumFieldDecorator(driver), pageClass);
            return pageClass.newInstance();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Deprecated
    public static <T> T instantiateObject(Class<T> pageClass) {
        try {
            return pageClass.newInstance();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
