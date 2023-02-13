package org.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import java.lang.annotation.Annotation;

public class AnnotationUtils {

    static boolean isElementPresented(ExtensionContext context, Class<? extends Annotation> annotation) {
        return context.getElement().isPresent() && context.getElement().get().isAnnotationPresent(annotation);
    }

    static boolean isAnnotationPresented(ExtensionContext context, Class<? extends Annotation> annotation) {
        return context.getRequiredTestClass().isAnnotationPresent(annotation) || context.getRequiredTestMethod().isAnnotationPresent(annotation);
    }
}
