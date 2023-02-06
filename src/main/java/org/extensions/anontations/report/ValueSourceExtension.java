package org.extensions.anontations.report;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValueSourceExtension {
    Class<?>[] classes();
}
