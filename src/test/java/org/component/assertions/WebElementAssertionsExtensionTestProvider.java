package org.component.assertions;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.assertions.AssertJExtensionProvider;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebElement;
import org.utils.assertions.AssertJHandler;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfNestedElementLocatedBy;

@Slf4j
@ReportConfiguration
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, AssertJExtensionProvider.class })
public class WebElementAssertionsExtensionTestProvider {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifySoftAssertion(AssertJHandler assertions) {
       // assertions.setWebElementAssertion(new SeleniumWebDriverProvider("", Duration.ofSeconds(3), new ChromeDriver()));
        Condition<WebElement> condition = new Condition<>(WebElement::isDisplayed, "");
       // assertions.assertElement(elementToBeClickable(By.id("aaa"))).is(condition);
       // assertions.assertElement(presenceOfNestedElementLocatedBy(By.id(""), By.id("aaa"))).is(condition);
       // assertions.assertElementText(elementToBeClickable(By.id(""))).containsOnlyDigits();
    }
}
