package org.poc.rest;

import com.aventstack.extentreports.AnalysisStrategy;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.rest.RestStep;
import org.extensions.report.ExtentReportExtension;
import org.extensions.rest.RestAssuredBuilderExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.rest.assured.ResponseObject;

import java.util.Map;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { ExtentReportExtension.class, RestAssuredBuilderExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST,
        mongoConnection = "project.mongo.connection"
)
public class RestAssuredExtensionTest {

    @Test
    @RestDataProvider(baseUri = "", restStep = {
            @RestStep(
                    stepId = 1,
                    contentType = ContentType.ANY,
                    method = Method.GET,
                    path = "",
                    value = String.class
            ),
            @RestStep(
                    stepId = 2,
                    contentType = ContentType.ANY,
                    method = Method.GET,
                    path = "",
                    value = String.class
            )
    })
    public void testRestCalls(Map<Integer, ResponseObject<String>> responseObject) {
        log.info(responseObject.get(1).getResponseToObject());
    }
}
