package org.component.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.utils.assertions.AssertionsManager;
import org.utils.assertions.AssertionsLevel;
import org.extensions.assertions.AssertionsExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
@ReportConfiguration
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, AssertionsExtension.class })
public class AssertionsExtensionLevelTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifySoftAssertion(AssertionsManager assertions) {
        assertions.setAssertionLevel(AssertionsLevel.SOFT);
        assertions.assertThat("aviad").isEqualTo("aviad");
        assertions.assertThat("aviad").isEqualTo("avi");
        assertions.assertThat("aviad").isEqualTo("avia");
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifyHardAssertionAfterSingleAssertionError(AssertionsManager assertions) {
        assertions.setAssertionLevel(AssertionsLevel.HARD_AFTER_ERROR);
        assertions.assertThat("aviad").isEqualTo("avi");
        assertions.assertThat("aviad").isEqualTo("aviad");
        assertions.assertThat("aviad").isEqualTo("avi");
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifyHardAssertionAfterEachTest(AssertionsManager assertions) {
        assertions.setAssertionLevel(AssertionsLevel.HARD_AFTER_TEST);
        assertions.assertThat("aviad").isEqualTo("avi");
        assertions.assertThat("aviad").isEqualTo("aviad");
        assertions.assertThat("aviad").isEqualTo("aviaaa");
    }
}
