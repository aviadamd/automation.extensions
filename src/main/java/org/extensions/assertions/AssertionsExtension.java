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

    private final ThreadLocal<AssertionsManager> assertionsHelper = new ThreadLocal<>();

    /**
     * beforeTestExecution
     * set the assertion manager instance
     * reset the error assertion collector list
     * set the assertion level to hard assert after error
     * @param context provide reflection metadata for the test
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            this.assertionsHelper.set(new AssertionsManager());
            this.assertionsHelper.get().setAssertionErrors(new ArrayList<>());
            this.assertionsHelper.get().setHardAssert(AssertionsLevel.HARD_AFTER_ERROR);
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
            if (this.assertionsHelper.get().getAssertionsLevel().equals(AssertionsLevel.HARD_AFTER_TEST)) {
                this.assertionsHelper.get().failAll(assertionsHelper.get().getAssertionErrors());
            } else this.assertionsHelper.get().setAssertionErrors(new ArrayList<>());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        return parameterContext.getParameter().getType() == AssertionsManager.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context)  {
        return this.assertionsHelper.get();
    }
}
