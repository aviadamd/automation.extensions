package org.extensions.anontations.mobile;

import org.base.mobile.MobileDriverManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DriverProvider {
    String url();
    MobileDriverManager.MobileDriverType driverType();
    String jsonCapsPath();
}
