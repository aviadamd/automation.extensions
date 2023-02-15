package org.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.ReflectionUtils;

public class GenericTypedParameterResolver implements ParameterResolver {

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext extension) {
        return ReflectionUtils.newInstance(parameter.getParameter().getType());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext extension) {
        return parameter.getParameter().getType() != null;
    }

}

