package org.extensions.anontations.rest;

import io.restassured.http.ContentType;
import io.restassured.http.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RestStep {
    int stepId();
    ContentType contentType() default ContentType.ANY;
    int responseStatusCode();
    Method method();
    String urlPath() default "";
    String[] paramsKeys() default "";
    String[] paramsValues() default "";
    String[] headersKeys() default "";
    String[] headersValues() default "";
    String[] bodyKeys() default "";
    String[] bodyValues() default "";
    String[] receiveHeadersKeys() default "";

}

