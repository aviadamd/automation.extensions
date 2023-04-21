package org.component.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.assertions.AssertionsExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
@ReportConfiguration
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { ExtentReportExtension.class })
public class AssertionsExtensionTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifySoftAssertion() {
        AssertionsExtension assertionsExtension = new AssertionsExtension(false);
        assertionsExtension.assertThat("aviad").isEqualTo("avi");
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void verifyHardAssertion() {
        AssertionsExtension assertionsExtension = new AssertionsExtension(true);
        assertionsExtension.assertThat("aviad").isEqualTo("avi");
    }


}
