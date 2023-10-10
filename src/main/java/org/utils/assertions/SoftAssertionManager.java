package org.utils.assertions;

import com.aventstack.extentreports.Status;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.extensions.report.ExtentTestManager;

import java.util.ArrayList;
import java.util.List;

public class SoftAssertionManager extends SoftAssertions {

    private static List<AssertionError> assertionErrors = new ArrayList<>();
    private AssertionsLevel assertionsLevel = AssertionsLevel.HARD_AFTER_ERROR;

    protected synchronized void setAssertionErrors(List<AssertionError> assertionErrors) { SoftAssertionManager.assertionErrors = assertionErrors; }
    protected synchronized void setAssertionLevel(AssertionsLevel assertionsLevel) { this.assertionsLevel = assertionsLevel; }
    protected List<AssertionError> getAssertionErrors() { return assertionErrors; }
    protected AssertionsLevel getAssertionsLevel() { return this.assertionsLevel; }

    @Override
    public synchronized void collectAssertionError(AssertionError assertionError) {
        assertionErrors.add(assertionError);
        if (this.assertionsLevel.equals(AssertionsLevel.HARD_AFTER_ERROR)) {
            this.print(Status.FAIL, "assertion error " + assertionError.getMessage());
            Assertions.fail("assertion error " + assertionError.getMessage());
        } else this.print(Status.INFO, "assertion error " + assertionError.getMessage());
    }

    /**
     * print extent report print
     * to ExtentTestManager report if instance is not null
     */
    protected synchronized void print(Status status, String description) {
        try {
            ExtentTestManager.getInstance().log(status,description);
        } catch (Exception ignore) {}
    }

    /**
     * failAll
     * has assertionErrors
     */
    protected synchronized void failAll(List<AssertionError> assertionErrors) {
        if (assertionErrors.size() > 0) {
            SoftAssertionManager.assertionErrors = new ArrayList<>();
            Assertions.fail("assertion errors test fails " + assertionErrors);
        }
    }
}
