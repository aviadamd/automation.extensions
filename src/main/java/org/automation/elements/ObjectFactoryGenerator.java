package org.automation.elements;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.junit.platform.commons.util.ReflectionUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ObjectFactoryGenerator {

    public static <T> T instantiateObject(Class<T> pageClass) {
        try {
            return ReflectionUtils.newInstance(pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public static <T> T instantiateObject(Class<T> pageClass, Object... args) {
        try {
            return ReflectionUtils.newInstance(pageClass, args);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public static <T extends ObjectFactoryGenerator> T instantiateWebPage(WebDriver driver, Class<T> pageClass) {
        try {
            PageFactory.initElements(driver, pageClass);
            return ReflectionUtils.newInstance(pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public static <T extends ObjectFactoryGenerator> T instantiateMobilePage(WebDriver driver, Class<T> pageClass) {
        try {
            PageFactory.initElements(new AppiumFieldDecorator(driver), pageClass);
            return ReflectionUtils.newInstance(pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
