package org.extensions.anontations.web;

import org.openqa.selenium.WebDriver;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface WebDriverType {
    int durationTimeOuts();
    Class<? extends WebDriver> [] driversInstance();
}
