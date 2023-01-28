package org.extensions.anontations.report;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestReportInfo {
    int testId();
    String assignCategory();
    String assignAuthor();
    String assignDevice();
}
