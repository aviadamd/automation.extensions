package org.extensions.web;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.ExtentTestManager;
import org.openqa.selenium.support.events.WebDriverListener;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class WebDriverListenerImpl implements WebDriverListener {

    @Override
    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        String methodDesc = "";

        switch (method.getName()) {
            case "get" -> methodDesc = "open browser";
            case "getText" -> methodDesc = "get text";
            case "findElement" -> methodDesc = "find element";
            case "isEnabled" -> methodDesc = "verify is element enabled";
            case "isDisplayed" -> methodDesc = "verify is element displayed";
        }

        methodDesc = methodDesc.isEmpty() ? method.getName() : methodDesc;
        String argsPrint = args == null ? "" : "with " + Arrays.asList(args);
        this.print(methodDesc + " " + argsPrint);
    }

    /**
     * print
     * @param message will print to console and to extents report
     */
    private void print(String message) {
        try {
            log.debug(message);
            ExtentTestManager.getInstance().log(Status.INFO, message);
        } catch (Exception ignore) {}
    }
}