package org.extensions.anontations.web;

import org.openqa.selenium.WebDriver;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface WebDriverType {
    String baseUrl() default "";
    boolean isWithProxy();
    int generalTo();
    Class<? extends WebDriver> [] driversInstance();

}
