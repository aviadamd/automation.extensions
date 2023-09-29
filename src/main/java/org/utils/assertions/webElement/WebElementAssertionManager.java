package org.utils.assertions.webElement;

import org.assertj.core.api.StringAssert;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebDriverWaitExtensions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.utils.assertions.AssertJHandler;
import org.utils.assertions.AssertionsLevel;

public class WebElementAssertionManager extends AssertJHandler {
    private final SeleniumWebDriverProvider seleniumWebDriverProvider;

    public WebElementAssertionManager(SeleniumWebDriverProvider seleniumWebDriverProvider) {
        this.seleniumWebDriverProvider = seleniumWebDriverProvider;
    }

    public synchronized WebElementAssert assertElement(ExpectedCondition<WebElement> condition) {
        WebElement until = this.findWith(condition);
        return this.proxy(WebElementAssert.class, WebElement.class, until);
    }

    public synchronized StringAssert assertElementText(ExpectedCondition<WebElement> condition) {
        String byText = this.findWith(condition).getText();
        return this.proxy(StringAssert.class, String.class, byText);
    }
    public synchronized StringAssert assertElementText(ExpectedCondition<WebElement> condition, String attribute) {
        String byTextAttribute = this.findWith(condition).getAttribute(attribute);
        return this.proxy(StringAssert.class, String.class, byTextAttribute);
    }

    private synchronized WebElement findWith(ExpectedCondition<WebElement> condition) {
        WebDriverWaitExtensions waitExtensions = this.seleniumWebDriverProvider.getWaitExtensions();
        if (this.getAssertionsLevel() == AssertionsLevel.HARD_AFTER_ERROR)
            return waitExtensions.getWebDriverWait().until(condition);
        return waitExtensions.optionalFluentWait().until(condition);
    }
}
