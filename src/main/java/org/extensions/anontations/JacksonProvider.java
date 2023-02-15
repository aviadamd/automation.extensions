package org.extensions.anontations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface JacksonProvider {

    String dir();
    String fileName();
    Class<?> classObject();

}
