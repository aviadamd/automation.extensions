package org.extensions.factory;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class JunitAnnotationHandler {
    public interface TestInfoHandler {
        <T extends Annotation> Optional<T> readAnnotation(TestInfo testInfo, Class<T> annotation);
    }

    public interface ExtensionContextHandler {
        <T extends Annotation> Optional<T> readAnnotation(ExtensionContext context, Class<T> annotation);
    }
}
