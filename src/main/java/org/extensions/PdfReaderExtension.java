package org.extensions;

import org.filesUtils.pdfReader.PdfConnector;
import org.filesUtils.pdfReader.PdfReader;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.util.HashMap;
import java.util.Optional;

public class PdfReaderExtension implements BeforeEachCallback, AfterAllCallback {
    public static HashMap<Integer,PdfReader> pdfReader = new HashMap<>();

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            this.pdfConnector(extensionContext).ifPresent(connector -> {
                pdfReader.put(connector.fileId(), new PdfReader(connector.path()));
            });
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            this.pdfConnector(extensionContext).ifPresent(connector -> {
                pdfReader.get(connector.fileId()).build().close();
            });
        }
    }

    private Optional<PdfConnector> pdfConnector(ExtensionContext context) {
        return Optional.ofNullable(context.getRequiredTestClass().getAnnotation(PdfConnector.class));
    }

}
