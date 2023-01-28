package org.extensions.anontations;

import org.openqa.selenium.WebDriver;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface DriverProvider {
    Class<? extends WebDriver> [] drivers();
}


