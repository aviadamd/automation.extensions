package org.utils.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.base.OptionalWrapper;
import org.base.web.SeleniumWebDriverProvider;
import org.extensions.assertions.AssertionsLevel;
import org.extensions.report.ExtentTestManager;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class AssertionsManager extends SoftAssertions {
    public AssertionsManager() {}
    private AssertionsLevel assertionsLevel = AssertionsLevel.HARD_AFTER_ERROR;
    private List<AssertionError> assertionErrors;
    private WebElementAssertionManager webElementAssertion;
    public synchronized void setWebElementAssertionManager(SeleniumWebDriverProvider webDriverProvider) { this.webElementAssertion = new WebElementAssertionManager(webDriverProvider); }
    public synchronized void setAssertionErrors(List<AssertionError> assertionErrors) { this.assertionErrors = assertionErrors; }
    public synchronized void setHardAssert(AssertionsLevel assertionsLevel) { this.assertionsLevel = assertionsLevel; }
    public List<AssertionError> getAssertionErrors() { return this.assertionErrors; }
    public AssertionsLevel getAssertionsLevel() { return this.assertionsLevel; }
    public WebElementAssertionManager getWebElementAssertion() { return webElementAssertion; }

    /**
     * collectAssertionError
     * error listener error
     * @param assertionError single assert error
     */
    @Override
    public void collectAssertionError(AssertionError assertionError) {
        this.assertionErrors.add(assertionError);
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
        AtomicInteger assertionsErrorsNewCounter = new AtomicInteger(this.assertionErrors.size());
        assertion.accept(this);
        this.calculateOnFail(assertionsErrorsNewCounter.get(), onFail, findBy);
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
        if (onFail != null && this.assertionErrors.size() > assertionsErrorsNewCounter) {

            List<AssertionError> assertionErrorList = this.assertionErrors;

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
