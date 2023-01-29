package org.extensions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.extensions.report.ExtentReportExtension.extentTest;

@Slf4j
public class DriverEventListener implements WebDriverListener {

    @Override
    public void beforeGet(WebDriver driver, String url) {
        this.print(Status.INFO,"navigate to get " + url);
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        this.print(Status.INFO, "navigate to get " + url);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {

    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {

    }

    @Override
    public void afterGetPageSource(WebDriver driver, String result) {

    }
    @Override

    public void afterClose(WebDriver driver) {

    }
    @Override
    public void afterQuit(WebDriver driver) {

    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {

    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {

    }

    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {

    }

    @Override
    public void afterResetInputState(WebDriver driver) {
        WebDriverListener.super.afterResetInputState(driver);
    }

    @Override
    public void afterClick(WebElement element) {
        this.print(Status.INFO, "click on " + element.toString());
    }
    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {

    }
    @Override
    public void afterClear(WebElement element) {

    }

    @Override
    public void afterGetAttribute(WebElement element, String name, String result) {

    }
    @Override
    public void afterGetText(WebElement element, String result) {

    }
    @Override
    public void afterFindElement(WebElement element, By locator, WebElement result) {

    }
    @Override
    public void afterFindElements(WebElement element, By locator, List<WebElement> result) {

    }

    @Override
    public void afterBack(WebDriver.Navigation navigation) {

    }

    @Override
    public void afterForward(WebDriver.Navigation navigation) {

    }

    @Override
    public void afterAnyAlertCall(Alert alert, Method method, Object[] args, Object result) {

    }

    @Override
    public void afterAccept(Alert alert) {

    }

    @Override
    public void afterDismiss(Alert alert) {

    }

    @Override
    public void afterGetText(Alert alert, String result) {

    }

    @Override
    public void afterSendKeys(Alert alert, String text) {

    }

    @Override
    public void afterAddCookie(WebDriver.Options options, Cookie cookie) {

    }
    @Override
    public void afterDeleteCookieNamed(WebDriver.Options options, String name) {

    }
    @Override
    public void afterDeleteCookie(WebDriver.Options options, Cookie cookie) {

    }

    @Override
    public void afterDeleteAllCookies(WebDriver.Options options) {

    }
    @Override
    public void afterGetCookies(WebDriver.Options options, Set<Cookie> result) {

    }

    @Override
    public void afterGetCookieNamed(WebDriver.Options options, String name, Cookie result) {

    }

    private void print(Status status, String message) {
        try {
            log.info(status + " | " + message);
            if (extentTest != null) extentTest.log(status, message);
        } catch (Exception exception) {
            //
        }

    }
}