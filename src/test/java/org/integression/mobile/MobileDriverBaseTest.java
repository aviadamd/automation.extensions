package org.integression.mobile;

import com.aventstack.extentreports.AnalysisStrategy;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.assertions.AssertionsExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@ExtendWith(value = { ExtentReportExtension.class  })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST)
public class MobileDriverBaseTest {


}
