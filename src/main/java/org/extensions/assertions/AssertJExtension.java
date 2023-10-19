package org.extensions.assertions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.utils.assertions.AssertJHandler;
import org.utils.assertions.AssertionsLevel;
import java.util.ArrayList;

public class AssertJExtension implements AfterEachCallback, BeforeTestExecutionCallback {

    private final ThreadLocal<AssertJHandler> assertionsManager = new ThreadLocal<>();

    /**
     * beforeTestExecution
     * set the assertion manager instance
     * reset the error assertion collector list
     * set the assertion level to hard assert after error
     * @param context provide reflection metadata for the test
     */
    @Override
    public synchronized void beforeTestExecution(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            this.assertionsManager.set(new AssertJHandler());
            this.assertionsManager.get().setAssertionErrors(new ArrayList<>());
            this.assertionsManager.get().setAssertionLevel(AssertionsLevel.HARD_AFTER_ERROR);
        }
    }

    /**
     * afterEach
     * will print all assertion error
     * will fail test by the next logic
     *   HARD_AFTER_TEST,
     *   HARD_AFTER_ERROR,
     *   SOFT
     * @param context provide reflection metadata for the test
     */
    @Override
    public synchronized void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            try {
                if (this.assertionsManager.get() != null) {
                    if (this.assertionsManager.get().getAssertionsLevel() != null &&
                            this.assertionsManager.get().getAssertionsLevel().equals(AssertionsLevel.HARD_AFTER_TEST)) {
                        this.assertionsManager.get().failAll();
                    } else this.assertionsManager.get().setAssertionErrors(new ArrayList<>());
                }
            } catch (Exception ignore) {}
        }
    }
}
