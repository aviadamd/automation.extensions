package org.component.assertions;

import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.assertions.AssertJExtensionProvider;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
@ReportSetUp
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, AssertJExtensionProvider.class })
public class AssertJExtensionProviderLevelTest {

//    @Test
//    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
//    void verifySoftAssertion(AssertJHandler assertions) {
//        assertions.setAssertionLevel(AssertionsLevel.SOFT);
//        assertions.assertThat("aviad").isEqualTo("aviad");
//        assertions.assertThat("aviad").isEqualTo("avi");
//        assertions.assertThat("aviad").isEqualTo("avia");
//    }
//
//    @Test
//    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
//    void verifyHardAssertionAfterSingleAssertionError(AssertJHandler assertions) {
//        assertions.setAssertionLevel(AssertionsLevel.HARD_AFTER_ERROR);
//        assertions.assertThat("aviad").isEqualTo("avi");
//        assertions.assertThat("aviad").isEqualTo("aviad");
//        assertions.assertThat("aviad").isEqualTo("avi");
//    }
//
//    @Test
//    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
//    void verifyHardAssertionAfterEachTest(AssertJHandler assertions) {
//        assertions.setAssertionLevel(AssertionsLevel.HARD_AFTER_TEST);
//        assertions.assertThat("aviad").isEqualTo("avi");
//        assertions.assertThat("aviad").isEqualTo("aviad");
//        assertions.assertThat("aviad").isEqualTo("aviaaa");
//    }
}
