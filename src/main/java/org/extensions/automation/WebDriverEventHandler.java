package org.extensions.automation;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.ExtentTestManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverListener;
import java.util.Arrays;

@Slf4j
public class WebDriverEventHandler implements WebDriverListener {
    @Override
    public void afterGet(WebDriver driver, String url) {
        this.print("navigate to get " + url);
        ExtentTestManager.logScreenShot(Status.PASS, driver, "navigate to " + url + " pass");
    }

    @Override
    public void afterFindElement(WebElement element, By locator, WebElement result) {
        if (element != null) this.print("find element " + element.getText());
        if (locator != null) this.print("find element " + locator);
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
    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        this.print("before execute script " + script + " with args " + Arrays.toString(args));
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

    private void print(String message) {
        try {
            log.info(Status.INFO + " | " + message);
            ExtentTestManager.log(Status.INFO, message);
        } catch (Exception ignore) {}
    }
}