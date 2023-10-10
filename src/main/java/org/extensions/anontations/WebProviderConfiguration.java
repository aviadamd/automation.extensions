package org.extensions.anontations;

import org.extensions.anontations.web.WebDriverType;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebProviderConfiguration {
    WebDriverType driverProvider();

}
