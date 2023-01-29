package org.extensions.mobile;

import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import java.util.concurrent.atomic.AtomicReference;
public class CapabilitiesParameterResolver extends ExtentReportExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == CapsReaderAdapter.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)  {
        try {
            if (extensionContext.getElement().isPresent() && parameterContext.getParameter().getType() == CapsReaderAdapter.class) {
                AtomicReference<CapsReaderAdapter> capsReaderAdapter = new AtomicReference<>(new CapsReaderAdapter());
                return capsReaderAdapter.get();
            } else Assertions.fail("Fail resolveParameterCapsReaderAdapter");
        } catch (Exception exception) {
            Assertions.fail("Fail resolveParameterCapsReaderAdapter", exception);
        }
        return null;
    }
}
