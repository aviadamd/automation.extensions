package org.base.anontations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.time.Duration;
import java.util.List;

public interface WebElementGestures {
    void click(WebElement element);
    void click(ExpectedCondition<WebElement> expectedCondition);
    void click(ExpectedCondition<WebElement> expectedCondition, Duration generalTimeOut, Duration pollingEvery);
    void sendKeys(WebElement element, CharSequence... keysToSend);
    void sendKeys(ExpectedCondition<WebElement> expectedCondition, CharSequence... keysToSend);
    String getAttribute(WebElement element, String name);
    String getText(WebElement element);
    List<WebElement> findElements(By by);
    WebElement findElement(By by);
    String getPageSource();
}
