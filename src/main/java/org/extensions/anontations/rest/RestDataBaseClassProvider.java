package org.extensions.anontations.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RestDataBaseClassProvider {
    String jsonPath() default "";
    String scheme() default "";
    String basePath() default "";
    String[] headersKeys() default {};
    String[] headersValues() default {};
}
