package org.extensions.assertions;

/*
   AssertionsExtension
   ThreadLocal wrap for support parallel execution
   * This class is using assertj SoftAssertions for managing the tests instance for more assertions level options
   * Using junit 5 extensions for control the test life cycle with assertion verification with the next options:
   1. hard assertion and fail test on the first assertion error
   2. hard assertion and fail test after test completed
   3. soft assertion without fail test
 */

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.extension.*;
import org.utils.assertions.AssertionsLevel;
import org.utils.assertions.AssertionsManager;
import java.util.ArrayList;

@Slf4j
public class AssertionsExtension extends SoftAssertions implements AfterEachCallback, BeforeTestExecutionCallback, ParameterResolver {

    private final ThreadLocal<AssertionsManager> assertionsManager = new ThreadLocal<>();

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
            this.assertionsManager.set(new AssertionsManager());
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
                        this.assertionsManager.get().failAll(assertionsManager.get().getAssertionErrors());
                    } else this.assertionsManager.get().setAssertionErrors(new ArrayList<>());
                }
            } catch (Exception ignore) {}
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        return parameterContext.getParameter().getType() == AssertionsManager.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context)  {
        return this.assertionsManager.get();
    }
}
