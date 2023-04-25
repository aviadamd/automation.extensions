package org.extensions.anontations.mobile;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DriverProvider {
    String jsonCapsPath();
    int proxyPort();
}
