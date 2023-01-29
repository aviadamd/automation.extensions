package org.automation.base;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

public interface WebElementGestures {
    void click(WebElement element);
    void click(ExpectedCondition<WebElement> expectedCondition);
    void click(List<ExpectedCondition<WebElement>> expectedConditions);
    void sendKeys(WebElement element, CharSequence... keysToSend);
    String getAttribute(WebElement element, String name);
    String getText(WebElement element);
}
