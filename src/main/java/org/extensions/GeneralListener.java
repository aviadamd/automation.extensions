package org.extensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

@Slf4j
public class GeneralListener implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info(context.getRequiredTestClass().getSimpleName() +"."+ context.getRequiredTestMethod().getName() + " pass");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        this.printErrors(context, throwable);
    }
    private void printErrors(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            log.error(context.getRequiredTestClass()+" class exception "+throwable.getMessage()+" from method "+context.getRequiredTestMethod().getName());
        }
    }

}
