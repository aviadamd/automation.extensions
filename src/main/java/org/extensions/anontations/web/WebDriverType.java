package org.extensions.anontations.web;

import org.base.web.DurationOf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface WebDriverType {
    String baseUrl() default "";
    DurationOf durationOf() default DurationOf.SECONDS;
    int generalTo() default 10;
    String driversInstance();
}
