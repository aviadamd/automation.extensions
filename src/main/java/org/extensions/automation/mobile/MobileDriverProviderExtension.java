package org.extensions.automation.mobile;

import org.automation.base.mobile.MobileDriverManager;
import org.extensions.anontations.mobile.DriverJsonProvider;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import java.util.HashMap;
import java.util.Optional;

public class MobileDriverProviderExtension extends ExtentReportExtension implements ParameterResolver {

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
        return parameterContext.getParameter().getType() == MobileDriverManager.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        HashMap<Long, MobileDriverManager> manager = new HashMap<>();
        try {
            if (context.getElement().isPresent() && parameterContext.getParameter().getType() == MobileDriverManager.class) {
                Optional<DriverJsonProvider> provider = this.readAnnotation(context, DriverJsonProvider.class);
                if (provider.isPresent()) {
                    manager.put(Thread.currentThread().getId(), new MobileDriverManager(Thread.currentThread().getId(), new CapsReaderAdapter(System.getProperty(provider.get().jsonCapsPath()))));
                    return manager.get(Thread.currentThread().getId());
                }
            } else throw new RuntimeException("MobileDriverProviderExtension error DriverJsonProvider");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return new HashMap<>();
    }
}
