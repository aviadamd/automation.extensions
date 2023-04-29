package org.utils.assertions;

import org.assertj.core.api.StringAssert;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebDriverWaitExtensions;
import org.extensions.assertions.AssertionsLevel;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class WebElementAssertionManager extends AssertionsManager {
    private final SeleniumWebDriverProvider seleniumWebDriverProvider;

    public WebElementAssertionManager(SeleniumWebDriverProvider seleniumWebDriverProvider) {
        this.seleniumWebDriverProvider = seleniumWebDriverProvider;
    }

    public WebElementAssert assertElement(ExpectedCondition<WebElement> condition) {
        WebElement until = this.findWith(condition);
        return proxy(WebElementAssert.class, WebElement.class, until);
    }

    public StringAssert assertElementText(ExpectedCondition<WebElement> condition) {
        String byText = this.findWith(condition).getText();
        return proxy(StringAssert.class, String.class, byText);
    }

    public StringAssert assertElementText(ExpectedCondition<WebElement> condition, String attribute) {
        String byTextAttribute = this.findWith(condition).getAttribute(attribute);
        return proxy(StringAssert.class, String.class, byTextAttribute);
    }

    private WebElement findWith(ExpectedCondition<WebElement> condition) {
        WebDriverWaitExtensions waitExtensions = this.seleniumWebDriverProvider.getWaitExtensions();
        if (this.getAssertionsLevel() == AssertionsLevel.HARD_AFTER_ERROR)
            return waitExtensions.getWebDriverWait().until(condition);
        return waitExtensions.optionalFluentWait().until(condition);
    }
}
