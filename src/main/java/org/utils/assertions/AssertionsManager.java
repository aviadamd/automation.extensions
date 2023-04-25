package org.utils.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.extensions.assertions.AssertionsLevel;
import org.extensions.report.ExtentTestManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AssertionsManager extends SoftAssertions {

    private AssertionsLevel assertionsLevel = AssertionsLevel.SOFT;
    private final List<AssertionError> assertionErrors;
    public AssertionsManager() { this.assertionErrors = new ArrayList<>(); }
    public List<AssertionError> getAssertionErrors() { return this.assertionErrors; }
    public AssertionsLevel getAssertionsLevel() {
        return this.assertionsLevel;
    }

    /**
     * control the level of the assertion
     * @param assertionsLevel
     *   HARD_AFTER_TEST,
     *   HARD_AFTER_ERROR,
     *   SOFT
     */
    public void setHardAssert(AssertionsLevel assertionsLevel) {
        this.assertionsLevel = assertionsLevel;
    }

    /**
     * collectAssertionError
     * error listener error
     * @param assertionError single assert error
     */
    @Override
    public void collectAssertionError(AssertionError assertionError) {
        this.assertionErrors.add(assertionError);
        if (this.assertionsLevel.equals(AssertionsLevel.HARD_AFTER_ERROR)) {
            this.printError(Status.FAIL,"assertion error" + assertionError.getMessage());
            Assertions.fail("assertion error: " + assertionError.getMessage());
        } else this.printError(Status.INFO,"assertion error" + assertionError.getMessage());
    }

    /**
     * failAll
     * has assertionErrors
     */
    public void failAll(List<AssertionError> assertionErrors) {
        if (assertionErrors.size() > 0) {
            assertionErrors.forEach(error -> this.printError(Status.FAIL,"assertion error" + error.getMessage()));
            Assertions.fail("assertion errors test fail");
        }
    }
    /**
     * print printError
     * to ExtentTestManager report if instance is not null
     */
    private synchronized void printError(Status status, String description) {
        try {
            ExtentTestManager.log(status, "assertion fails " + description);
        } catch (Exception ignore) {
            log.info("assertion fails");
        }
    }
}
