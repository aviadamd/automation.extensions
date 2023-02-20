package org.extensions.automation;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.ExtentTestManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

@Slf4j
public class WebDriverEventHandler implements WebDriverListener {

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        this.print(Status.FAIL,"driver event listener on error trigger " + e.getMessage());
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        this.print(Status.INFO,"navigate to get " + url);
    }
    @Override
    public void afterFindElement(WebElement element, By locator, WebElement result) {
        if (element != null) this.print(Status.INFO,"find element " + element.getText());
        if (locator != null) this.print(Status.INFO,"find element " + locator);
    }
    @Override
    public void afterClose(WebDriver driver) {
        if (driver != null) this.print(Status.INFO,"close " + driver);
    }
    @Override
    public void afterQuit(WebDriver driver) {
        if (driver != null) this.print(Status.INFO,"quit " + driver);
    }
    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {}
    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {}
    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {}
    @Override
    public void afterResetInputState(WebDriver driver) {}
    @Override
    public void beforeClick(WebElement element) {
        if (element != null) this.print(Status.INFO,"about to click on " + element.getText());
    }
    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {}
    @Override
    public void afterClear(WebElement element) {}
    @Override
    public void afterGetAttribute(WebElement element, String name, String result) {}
    @Override
    public void afterGetText(WebElement element, String result) {
        this.print(Status.INFO,"get text from " + element.getText());
    }
    @Override
    public void afterBack(WebDriver.Navigation navigation) {}
    @Override
    public void afterForward(WebDriver.Navigation navigation) {}
    @Override
    public void afterAddCookie(WebDriver.Options options, Cookie cookie) {}
    @Override
    public void afterDeleteCookieNamed(WebDriver.Options options, String name) {}
    @Override
    public void afterDeleteCookie(WebDriver.Options options, Cookie cookie) {}
    @Override
    public void afterDeleteAllCookies(WebDriver.Options options) {}
    @Override
    public void afterGetCookies(WebDriver.Options options, Set<Cookie> result) {}
    @Override
    public void afterGetCookieNamed(WebDriver.Options options, String name, Cookie result) {}

    private void print(Status status, String message) {
        try {
            log.info(Status.INFO + " | " + message);
            ExtentTestManager.log(status,message);
        } catch (Exception ignore) {}
    }

    private void screenShot(Status status, WebDriver driver, String message) {
        try {
            log.info(Status.INFO + " | " + message);
            ExtentTestManager.log(status, ExtentTestManager.getScreenShot(driver, message));
        } catch (Exception ignore) {}
    }

}