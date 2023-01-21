package org.extensions.anontations.appium;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AppiumServerArgumentsInjections {
    AndroidServerArgumentsInjections[] android();
    IosServerArgumentsInjections[] ios();
}
