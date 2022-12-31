package org.poc.pdf;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.PdfReaderExtension;
import org.extensions.ExtentReportListener;
import org.filesUtils.pdfReader.PdfConnector;
import org.filesUtils.pdfReader.PdfReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;

import static org.extensions.PdfReaderExtension.pdfReader;

@Slf4j
@ExtendWith(value = { ExtentReportListener.class, PdfReaderExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        generateExtraReportsBy = { Status.FAIL, Status.SKIP },
        reportJsonSettingsPath = "src/main/resources/reportConfig.json")
@PdfConnector(fileId = 1, path = "C:\\Users\\Lenovo\\IdeaProjects\\mobile.automation.extensions\\src\\test\\resources\\sample.pdf")
public class PdfReaderTest {

    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void testPdfReader() {
        PdfReader.Actions action = pdfReader
                .get(1)
                .step(actions -> {
                    log.info("pdf text " + actions.getText());
                    actions.writeText("shit is not good");
                    log.info("pdf text " + actions.getText());
                }).build();
        log.info(action.getText());
    }

}
