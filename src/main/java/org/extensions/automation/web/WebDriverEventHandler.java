package org.extensions.automation.web;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.ExtentTestManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class WebDriverEventHandler implements WebDriverListener {
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriverEventHandler(WebDriver driver) {
        this.driver.set(driver);
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException error) {
        if(driver.get() != null) {
            this.print("target " + target + " error, from method " + method.getName()
                    + ", with args " + Arrays.asList(args) + " with error " + error.getMessage());
        }
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        if(driver != null) this.screenShot(driver,"navigate to " + url);
    }

    @Override
    public void afterFindElement(WebElement element, By locator, WebElement result) {
        if (locator != null) this.screenShot(this.driver.get(),"find element " + locator);

    }

    @Override
    public void afterClose(WebDriver driver) {
        this.print("close driver session");
    }

    @Override
    public void afterQuit(WebDriver driver) {
        this.print("quit driver session");
    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        this.print("finish execute script " + script + " with args " + Arrays.toString(args) + ", with result " + result);
    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
        this.print("finish execute script " + script + " with args " + Arrays.toString(args) + ", with result " + result);
    }

    @Override
    public void beforeClick(WebElement element) {
        if (element != null) this.print("about to click on " + element.getText());
    }

    @Override
    public void afterGetText(WebElement element, String result) {
        if (element != null) this.print("get text from " + element.getText());
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        this.print("send keys text " + Arrays.toString(keysToSend) +  " to " + element.getText());
    }

    /**
     * print
     * @param message
     */
    private void print(String message) {
        try {
            ExtentTestManager.log(Status.INFO, message);
        } catch (Exception ignore) {}
    }

    /**
     * screenShot
     * @param driver
     * @param message
     */
    private void screenShot(WebDriver driver, String message) {
        try {
            this.print(message);
            if (driver != null) ExtentTestManager.logScreenShot(Status.PASS, driver, message, false);
        } catch (Exception ignore) {}
    }
}