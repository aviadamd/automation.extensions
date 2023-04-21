package org.extensions.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.extensions.report.ExtentTestManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.util.List;

@Slf4j
public class AssertionsExtension extends SoftAssertions {
    private final boolean hardAssert;
    private final SoftAssertions softAssertions;

    /**
     * control the level of the assertion
     * @param hardAssert or soft assert
     */
    public AssertionsExtension(boolean hardAssert) {
        this.hardAssert = hardAssert;
        this.softAssertions = new SoftAssertions();
    }

    @Override
    public void collectAssertionError(AssertionError assertionError) {
        if (this.hardAssert) {
            softAssertions.collectAssertionError(assertionError);
        }
    }

    @Override
    public void onAssertionErrorCollected(AssertionError assertionError) {
        if (this.hardAssert) {
            List<AssertionError> assertionErrors = softAssertions.assertionErrorsCollected();
            this.print(Status.INFO, "found assertions errors");
            Assertions.fail("assertions info: " + assertionError.getMessage());
        } else {
            this.print(Status.INFO, "found assertions errors");
            this.print(Status.INFO, "assertions info: " + assertionError.getMessage());
        }
    }

    public void afterEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            if (this.hardAssert) {
                List<AssertionError> assertionErrors = softAssertions.assertionErrorsCollected();
                if (assertionErrors.size() > 0) {
                    assertionErrors.forEach(assertionError -> {
                        this.print(Status.INFO, "found assertions errors");
                        this.print(Status.FAIL, "assertions info: " + assertionError.getMessage());
                    });
                    Assertions.fail("");
                }
            }
        }
    }

    /**
     * print
     * to ExtentTestManager report if instance is not null
     * @param status
     *   INFO("Info", 10),
     *   PASS("Pass", 20),
     *   WARNING("Warning", 30),
     *   SKIP("Skip", 40),
     *   FAIL("Fail", 50);
     * @param message to report
     */
    private synchronized void print(Status status, String message) {
        try {
            ExtentTestManager.log(status, message);
        } catch (Exception ignore) {
            log.info(message);
        }
    }
}
