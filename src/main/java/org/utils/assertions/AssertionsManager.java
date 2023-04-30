package org.utils.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.base.OptionalWrapper;
import org.base.web.SeleniumWebDriverProvider;
import org.extensions.report.ExtentTestManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class AssertionsManager extends SoftAssertions {

    private static List<AssertionError> assertionErrors;
    private WebElementAssertionManager webElementAssertion;
    private AssertionsLevel assertionsLevel = AssertionsLevel.HARD_AFTER_ERROR;

    public synchronized void setAssertionErrors(List<AssertionError> assertionErrors) { AssertionsManager.assertionErrors = assertionErrors; }
    public synchronized void setAssertionLevel(AssertionsLevel assertionsLevel) { this.assertionsLevel = assertionsLevel; }
    public synchronized void setWebElementAssertion(SeleniumWebDriverProvider seleniumWebDriverProvider) { this.webElementAssertion = new WebElementAssertionManager(seleniumWebDriverProvider); }

    public List<AssertionError> getAssertionErrors() { return assertionErrors; }
    public AssertionsLevel getAssertionsLevel() { return this.assertionsLevel; }

    /**
     * collectAssertionError
     * error listener error
     * @param assertionError single assert error
     */
    @Override
    public synchronized void collectAssertionError(AssertionError assertionError) {
        assertionErrors.add(assertionError);
        if (this.assertionsLevel.equals(AssertionsLevel.HARD_AFTER_ERROR)) {
            this.print(Status.FAIL, "assertion error " + assertionError.getMessage());
            Assertions.fail("assertion error " + assertionError.getMessage());
        } else this.print(Status.INFO, "assertion error " + assertionError.getMessage());
    }


    /**
     * assert with options
     * @param assertion pass as consumer
     * @param onFail by your logic
     */
    public synchronized void assertWith(Consumer<AssertionsManager> assertion, Predicate<String> findBy, Consumer<AssertionError> onFail) {
        AtomicInteger assertionsErrorsNewCounter = new AtomicInteger(assertionErrors.size());
        assertion.accept(this);
        this.calculateOnFail(assertionsErrorsNewCounter.get(), onFail, findBy);
    }

    public WebElementAssert assertElement(ExpectedCondition<WebElement> condition) {
        return this.webElementAssertion.assertElement(condition);
    }

    public StringAssert assertElementText(ExpectedCondition<WebElement> condition) {
        return this.webElementAssertion.assertElementText(condition);
    }

    public StringAssert assertElementText(ExpectedCondition<WebElement> condition, String attribute) {
        return this.webElementAssertion.assertElementText(condition, attribute);
    }

    /**
     * failAll
     * has assertionErrors
     */
    public synchronized void failAll(List<AssertionError> assertionErrors) {
        if (assertionErrors.size() > 0) {
            Assertions.fail("assertion errors test fails " + assertionErrors);
        }
    }

    /**
     * print extent report print
     * to ExtentTestManager report if instance is not null
     */
    public synchronized void print(Status status, String description) {
        try {
            ExtentTestManager.log(status, "assertion fails " + description);
        } catch (Exception ignore) {
            log.info("assertion fails");
        }
    }

    /**
     * calculateOnFail
     * @param assertionsErrorsNewCounter indication if there is a new error
     * @param onFail pass as consumer for fluent the test
     */
    private synchronized void calculateOnFail(int assertionsErrorsNewCounter, Consumer<AssertionError> onFail, Predicate<String> findBy) {
        if (onFail != null && assertionErrors.size() > assertionsErrorsNewCounter) {

            List<AssertionError> assertionErrorList = assertionErrors;

            if (findBy != null) {
                for (AssertionError error: assertionErrorList) {
                    if (findBy.test(error.getMessage())) {
                        onFail.accept(error);
                        if (this.assertionsLevel == AssertionsLevel.HARD_AFTER_ERROR) {
                            Assertions.fail(error.getMessage(), error);
                        }
                        break;
                    }
                }
            } else {
                new OptionalWrapper<>(assertionErrorList
                        .stream()
                        .skip(assertionErrorList.size() -1)
                        .findFirst())
                        .ifPresent(error -> {
                            onFail.accept(error);
                            if (this.assertionsLevel == AssertionsLevel.HARD_AFTER_ERROR) {
                                Assertions.fail(error.getMessage(), error);
                            }
                        })
                        .ifNotPresent(() -> log.info("no fail appear from assertion"));
            }
        }
    }
}
