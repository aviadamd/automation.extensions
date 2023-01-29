package org.extensions;

import org.extensions.anontations.ClassProvider;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ObjectsParametersResolver extends ExtentReportExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)  {
        if (extensionContext.getElement().isPresent()) {
            ClassProvider classProvider = extensionContext.getElement().get().getAnnotation(ClassProvider.class);
            return classProvider != null;
        }
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)  {
        try {

            if (extensionContext.getElement().isPresent()) {
                ClassProvider classProvider = extensionContext.getElement().get().getAnnotation(ClassProvider.class);
                return classProvider.classObject().newInstance();
            } else Assertions.fail("Fail ObjectsParametersResolver");
        } catch (Exception exception) {
            Assertions.fail("Fail ObjectsParametersResolver", exception);
        }
        return false;
    }
}
