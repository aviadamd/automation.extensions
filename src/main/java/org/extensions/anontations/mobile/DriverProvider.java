package org.extensions.anontations.mobile;

import org.extensions.mobile.ApplicationLaunchOption;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DriverProvider {
    String jsonCapabilitiesFile();
    int proxyPort();
    String [] iosExtraCapsKeys() default {};
    String [] iosExtraCapsValues() default {};
    String [] androidExtraCapsKeys() default {};
    String [] androidExtraValuesValues() default {};
    ApplicationLaunchOption appLaunchOption() default ApplicationLaunchOption.IGNORE;
    int implicitlyWait() default 10;

}
