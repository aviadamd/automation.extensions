package org.utils.assertions;

import com.aventstack.extentreports.Status;
import org.assertj.core.api.Assert;
import java.util.List;

public class AssertJHandler {

    private final SoftAssertionManager softAssertionManager;

    public AssertJHandler() {
        this.softAssertionManager = new SoftAssertionManager();
    }

    public synchronized void setAssertionErrors(List<AssertionError> assertionErrors) {
        this.softAssertionManager.setAssertionErrors(assertionErrors);
    }
    public synchronized void setAssertionLevel(AssertionsLevel assertionsLevel) {
        this.softAssertionManager.setAssertionLevel(assertionsLevel);
    }
    public AssertionsLevel getAssertionsLevel() { return this.softAssertionManager.getAssertionsLevel(); }

    public <T> void is(T actual, AssertJCondition<T> conditionsOptions) {
        this.softAssertionManager.assertThat(actual).is(conditionsOptions.condition());
        this.softAssertionManager.print(Status.PASS, "actual " + actual + " with condition " + conditionsOptions.description() + " is true");
    }

    public <ASSERTION_CLASS extends Assert<? extends ASSERTION_CLASS, ? extends ACTUAL>, ACTUAL> ASSERTION_CLASS proxy(
            Class<ASSERTION_CLASS> assertClass, Class<ACTUAL> actualClass, ACTUAL actual) {
        return this.softAssertionManager.proxy(assertClass, actualClass, actual);
    }

    /**
     * failAll
     * has assertionErrors
     */
    public synchronized void failAll() {
        this.softAssertionManager.failAll(this.softAssertionManager.getAssertionErrors());
    }
}
