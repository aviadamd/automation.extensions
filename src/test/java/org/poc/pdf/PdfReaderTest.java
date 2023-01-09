package org.poc.pdf;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.PdfReaderExtension;
import org.extensions.ExtentReportListener;
import org.extensions.anontations.pdf.PdfConnector;
import org.extensions.anontations.pdf.PdfFileConfig;
import org.files.pdfReader.PdfReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import static org.extensions.ExtentReportListener.extentTest;
import static org.extensions.PdfReaderExtension.pdfReader;

@Slf4j
@ExtendWith(value = { ExtentReportListener.class, PdfReaderExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        generateExtraReportsBy = { Status.FAIL, Status.SKIP },
        reportJsonSettingsPath = "src/main/resources/reportConfig.json")
@PdfConnector(pdfFileConfig = {
        @PdfFileConfig(fileId = 1, path = "src/test/resources/sample.pdf"),
        @PdfFileConfig(fileId = 2, path = "src/test/resources/sample.pdf")
})
public class PdfReaderTest {

    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void testPdfReader1() {
        PdfReader.Actions action = pdfReader
                .get(1)
                .step(actions -> {
                    extentTest.info("pdf text " + actions.getText());
                    actions.writeText("shit is not good");
                    extentTest.info("pdf text " + actions.getText());
                }).build();
        log.info(action.getText());
    }

    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void testPdfReader2() {
        PdfReader.Actions action = pdfReader
                .get(2)
                .step(actions -> {
                    extentTest.info("pdf text " + actions.getText());
                    actions.writeText("shit is not good");
                    extentTest.info("pdf text " + actions.getText());
                }).build();
        log.info(action.getText());
    }
}
