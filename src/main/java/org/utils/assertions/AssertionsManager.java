package org.utils.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.assertj.core.description.Description;
import org.extensions.report.ExtentTestManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AssertionsManager extends SoftAssertions {
    private static List<AssertionError> assertionErrors = new ArrayList<>();
    private AssertionsLevel assertionsLevel = AssertionsLevel.HARD_AFTER_ERROR;

    public synchronized void setAssertionErrors(List<AssertionError> assertionErrors) { AssertionsManager.assertionErrors = assertionErrors; }
    public synchronized void setAssertionLevel(AssertionsLevel assertionsLevel) { this.assertionsLevel = assertionsLevel; }

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

    public <E> void is(E expected, AssertCondition<E> assertCondition) {
        super.assertThat(expected).is(assertCondition.getCondition());
        Description description = assertCondition.getCondition().description();
        this.print(Status.PASS, "expected " + expected + " with condition " + description + " is true");
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
            ExtentTestManager.log(status,description);
        } catch (Exception ignore) {
            log.info(description);
        }
    }
}
