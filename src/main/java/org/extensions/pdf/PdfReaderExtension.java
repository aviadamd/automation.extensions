package org.extensions.pdf;

import org.extensions.anontations.pdf.PdfConnector;
import org.extensions.anontations.pdf.PdfFileConfig;
import org.utils.files.pdfReader.PdfReader;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.util.HashMap;
import java.util.Optional;

public class PdfReaderExtension implements
        BeforeEachCallback,
        AfterAllCallback {

    private static final HashMap<Integer,PdfReader> pdfReader = new HashMap<>();
    public HashMap<Integer, PdfReader> getPdfReader() { return pdfReader; }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            this.pdfConnector(extensionContext).ifPresent(connector -> {
                for (PdfFileConfig pdfFileConfig: connector.pdfFileConfig()) {
                    pdfReader.put(pdfFileConfig.fileId(), new PdfReader(pdfFileConfig.path()));
                }
            });
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (extensionContext.getElement().isPresent()) {
            this.pdfConnector(extensionContext).ifPresent(connector -> {
                for (PdfFileConfig pdfFileConfig: connector.pdfFileConfig()) {
                    pdfReader.get(pdfFileConfig.fileId()).close();
                }
            });
        }
    }

    private Optional<PdfConnector> pdfConnector(ExtensionContext context) {
        return Optional.ofNullable(context.getRequiredTestClass().getAnnotation(PdfConnector.class));
    }

}
