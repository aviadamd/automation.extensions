package org.base.web;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.ExtentTestManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

@Slf4j
public class WebDriverScrollExtension {
    private final SeleniumWebDriverProvider seleniumWebDriverProvider;

    public WebDriverScrollExtension(SeleniumWebDriverProvider seleniumWebDriverProvider) {
        this.seleniumWebDriverProvider = seleniumWebDriverProvider;
    }

    public JavascriptExecutor javascriptExecutor() {
        return (JavascriptExecutor) this.seleniumWebDriverProvider.getDriver();
    }

    public SeleniumWebDriverProvider scrollDown() {
        this.javascriptExecutor().executeScript("window.scrollBy(0,500)");
        return this.seleniumWebDriverProvider;
    }

    public SeleniumWebDriverProvider scrollUp() {
        this.javascriptExecutor().executeScript("window.scrollBy(0,-500)");
        return this.seleniumWebDriverProvider;
    }

    public void scroll(ScrollDirection direction) {
        switch (direction) {
            case UP -> this.scrollDown();
            case DOWN -> this.scrollUp();
        }
    }
    public SeleniumWebDriverProvider scrollToElement(int search, ScrollDirection direction, ExpectedCondition<WebElement> condition) {
        WebElement searchElement = null;

        for (int i = 0; i < search; i++) {
            try {
                searchElement = this.seleniumWebDriverProvider
                        .getWaitExtensions()
                        .optionalFluentWait()
                        .until(condition);
                if (searchElement != null) break;
                this.scroll(direction);
            } catch (Exception ignore) {}
        }

        this.searchResult(searchElement, direction, condition);
        return this.seleniumWebDriverProvider;
    }

    private void searchResult(WebElement searchElement, ScrollDirection direction, ExpectedCondition<WebElement> condition) {
        if (searchElement == null) {
            Assertions.fail("fail to find " + condition.toString() + " with scroll " + direction + " to element");
        } else this.print("find " + condition.toString() + ", with scroll " + direction + " to element");
    }

    private void print(String message) {
        try {
            log.info(Status.INFO + " | " + message);
            ExtentTestManager.getInstance().log(Status.INFO, message);
        } catch (Exception ignore) {}
    }
}
