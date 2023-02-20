package org.extensions.anontations;

import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.web.WebDriverType;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProviderConfiguration {
    JacksonProvider jacksonProvider();
    WebDriverType driverProvider();
    MongoMorphiaConnector dbProvider();

}
