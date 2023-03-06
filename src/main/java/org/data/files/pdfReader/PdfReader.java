package org.data.files.pdfReader;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public class PdfReader {
    private Actions build;
    private PDDocument document;
    protected static String filePath;

    public PdfReader(String path) {
        try {
            this.document = PDDocument.load(new File(path));
            filePath = path;
        } catch (Exception exception) {
            log.error("PdfReaderExtensions load file error " + exception.getMessage());
        }
    }
    public PdfReader step(Consumer<Actions> action) {
        try {
            this.build = new Actions(this.document, new PDFTextStripper());
            action.accept(this.build);
        } catch (Exception exception) {
            log.error("PdfReaderExtensions step error " + exception.getMessage());
        }
        return this;
    }

    public Actions build() {
        return this.build;
    }

    public void close() {
        try {
            if (this.build != null) this.build().pdDocument.close();
        } catch (Exception exception) {
            log.error("PdfReaderExtensions close file error " + exception.getMessage());
        }
    }

    public static class Actions {
        private final PDDocument pdDocument;
        private final PDFTextStripper pdfTextStripper;
        public Actions(PDDocument pdDocument, PDFTextStripper pdfTextStripper) {
            this.pdDocument = pdDocument;
            this.pdfTextStripper = pdfTextStripper;
        }
        public PDDocument getPdDocument() { return pdDocument; }
        public PDFTextStripper getPdfTextStripper() { return pdfTextStripper; }

        public String getText() {
            try {
                return this.pdfTextStripper.getText(this.pdDocument);
            } catch (Exception exception) {
                log.error("PdfReaderExtensions get text exception " + exception.getMessage());
                return "";
            }
        }

        public Optional<PDPageTree> getPages() {
            try {
                return Optional.ofNullable(this.pdDocument.getPages());
            } catch (Exception exception) {
                return Optional.empty();
            }
        }

        public boolean removePage(PDPage page) {
            try {
                this.pdDocument.removePage(page);
                return true;
            } catch (Exception exception) {
                log.error("PdfReaderExtensions remove pdf page "+page+" exception " + exception.getMessage());
                return false;
            }
        }

        public boolean writeText(String data) {
            PDPage myPage = new PDPage();
            this.pdDocument.addPage(myPage);

            try (PDPageContentStream write = new PDPageContentStream(this.pdDocument, myPage)) {
                write.beginText();
                write.setFont(PDType1Font.TIMES_ROMAN, 12);
                write.setLeading(14.5f);
                write.newLineAtOffset(25, 700);
                write.showText(data);
                write.endText();
                this.save(filePath);
                return true;
            } catch (Exception exception) {
                log.error("PdfReaderExtensions write text exception " + exception.getMessage());
                return false;
            }
        }

        public boolean removePage(int page) {
            try {
                this.pdDocument.removePage(page);
                return true;
            } catch (Exception exception) {
                log.error("PdfReaderExtensions remove pdf page "+page+" exception " + exception.getMessage());
                return false;
            }
        }

        public boolean save(String path) {
            try {
                this.pdDocument.save(path);
                return true;
            } catch (Exception exception) {
                log.error("PdfReaderExtensions save pdf exception " + exception.getMessage());
                return false;
            }
        }
    }
}
