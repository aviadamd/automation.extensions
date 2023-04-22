package org.component.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.utils.assertions.AssertionsManager;
import org.extensions.assertions.AssertionsLevel;
import org.extensions.assertions.AssertionsExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
@ReportConfiguration
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { ExtentReportExtension.class, AssertionsExtension.class })
public class AssertionsExtensionTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifySoftAssertion(AssertionsManager assertions) {
        assertions.setHardAssert(AssertionsLevel.SOFT);
        assertions.assertThat("aviad").isEqualTo("avi");
        assertions.assertThat("aviad").isEqualTo("aviad");
        assertions.assertThat("aviad").isEqualTo("aviaaa");
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifyHardAssertionAfterSingleAssertionError(AssertionsManager assertions) {
        assertions.setHardAssert(AssertionsLevel.HARD_AFTER_ERROR);
        assertions.assertThat("aviad").isEqualTo("avi");
        assertions.assertThat("aviad").isEqualTo("aviad");
        assertions.assertThat("aviad").isEqualTo("avi");
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifyHardAssertionAfterEachTest(AssertionsManager assertions) {
        assertions.setHardAssert(AssertionsLevel.HARD_AFTER_TEST);
        assertions.assertThat("aviad").isEqualTo("avi");
        assertions.assertThat("aviad").isEqualTo("aviad");
        assertions.assertThat("aviad").isEqualTo("aviaaa");
    }
}
