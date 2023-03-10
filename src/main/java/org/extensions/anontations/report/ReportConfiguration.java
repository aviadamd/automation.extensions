package org.extensions.anontations.report;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ReportConfiguration {
    String reportPath();
    String reportSettingsPath();
    Status[] extraReportsBy() default Status.PASS;
    AnalysisStrategy analysisStrategy();
    String mongoConnection() default "";
}
