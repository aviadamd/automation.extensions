package org.poc.pdf;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
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

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@ExtendWith(value = { ExtentReportExtension.class, PdfReaderExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST,
        mongoConnection = "mongodb://localhost:27017"
)
@PdfConnector(pdfFileConfig = {
        @PdfFileConfig(fileId = 1, path = "src/test/resources/sample.pdf"),
        @PdfFileConfig(fileId = 2, path = "src/test/resources/sample.pdf")
})
public class PdfReaderTest {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    public void testPdfReader1() {
        PdfReader.Actions action = new PdfReaderExtension()
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
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    public void testPdfReader2() {
        PdfReader.Actions action = new PdfReaderExtension()
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
