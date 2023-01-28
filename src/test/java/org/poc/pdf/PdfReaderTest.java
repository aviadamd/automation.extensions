package org.poc.pdf;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.base.ObjectsBeans;
import org.extensions.pdf.PdfReaderExtension;
import org.extensions.report.ExtentReportExtension;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.pdf.PdfConnector;
import org.extensions.anontations.pdf.PdfFileConfig;
import org.files.pdfReader.PdfReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;

@Slf4j
@ExtendWith(value = { ExtentReportExtension.class, PdfReaderExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        extraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
@PdfConnector(pdfFileConfig = {
        @PdfFileConfig(fileId = 1, path = "src/test/resources/sample.pdf"),
        @PdfFileConfig(fileId = 2, path = "src/test/resources/sample.pdf")
})
public class PdfReaderTest extends ObjectsBeans {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    public void testPdfReader1() {
        PdfReader.Actions action = this.pdfReaderExtension()
                .getPdfReader()
                .get(1)
                .step(actions -> {
                    log.info("pdf text " + actions.getText());
                    actions.writeText("shit is not good");
                    log.info("pdf text " + actions.getText());
                }).build();
        log.info(action.getText());
    }

    @Test
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    public void testPdfReader2() {
        PdfReader.Actions action = this.pdfReaderExtension()
                .getPdfReader()
                .get(2)
                .step(actions -> {
                    log.info("pdf text " + actions.getText());
                    actions.writeText("shit is not good");
                    log.info("pdf text " + actions.getText());
                }).build();
        log.info(action.getText());
    }
}
