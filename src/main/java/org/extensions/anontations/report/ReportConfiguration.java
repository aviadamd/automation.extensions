package org.extensions.anontations.report;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtendWith(value = { ExtentReportExtension.class })
public @interface ReportConfiguration {
    String reportPath() default "project.report.path";
    String reportSettingsPath() default "project.report.config";
    Status[] extraReportsBy() default { Status.FAIL, Status.SKIP };
    AnalysisStrategy analysisStrategy() default AnalysisStrategy.CLASS;
    String mongoConnection() default "project.mongo.connection";
}
