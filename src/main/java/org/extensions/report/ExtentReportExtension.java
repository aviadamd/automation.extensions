package org.extensions.report;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import java.util.*;

@Slf4j
public class ExtentReportExtension implements TestWatcher, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    private final ThreadLocal<ExtentReportExtensionManager> extentReportExtensionManager = new ThreadLocal<>();

    @Override
    public synchronized void beforeAll(ExtensionContext context) {
        this.extentReportExtensionManager.set(new ExtentReportExtensionManager());
        this.extentReportExtensionManager.get().createReport(context);
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        this.extentReportExtensionManager.get().createTest(context);
    }

    @Override
    public synchronized void afterEach(ExtensionContext context) {
        this.extentReportExtensionManager.get().finishTest(context);
    }

    @Override
    public synchronized void testSuccessful(ExtensionContext context) {
        this.extentReportExtensionManager.get().onTestPass(context);
    }

    @Override
    public synchronized void testDisabled(ExtensionContext context, Optional<String> reason) {
        this.extentReportExtensionManager.get().onTestDisabled(context, reason);
    }

    @Override
    public synchronized void testAborted(ExtensionContext context, Throwable throwable) {
        this.extentReportExtensionManager.get().onTestSkip(context, throwable);
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        this.extentReportExtensionManager.get().onTestFail(context, throwable);
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        this.extentReportExtensionManager.get().createExtraReport(context);
        this.extentReportExtensionManager.get().flushExtentReport(context);
        this.extentReportExtensionManager.get().createMongoReport(context);
        this.extentReportExtensionManager.get().createJsonReport(context);
    }
}
