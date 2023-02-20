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
    public <T extends ObjectFactoryGenerator> void instantiateWebPage(WebDriver driver, T pageClass) {
        try {
            PageFactory.initElements(driver, pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public <T extends ObjectFactoryGenerator> void instantiateMobilePage(WebDriver driver, T pageClass) {
        try {
            PageFactory.initElements(new AppiumFieldDecorator(driver), pageClass);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
