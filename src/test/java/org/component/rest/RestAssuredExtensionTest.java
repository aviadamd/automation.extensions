package org.component.rest;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.rest.RestStep;
import org.extensions.report.ExtentReportExtension;
import org.extensions.rest.RestAssuredBuilderExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.utils.rest.assured.ResponseObject;

import java.util.Map;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { ExtentReportExtension.class, RestAssuredBuilderExtension.class })
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { FAIL, SKIP }, reportSettingsPath = "project.report.config", analysisStrategy = AnalysisStrategy.TEST, mongoConnection = "project.mongo.connection")
public class RestAssuredExtensionTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @RestDataProvider(baseUri = "sonplaceholder.typicode.com",
            restSteps = {
            @RestStep(
                    stepId = 1,
                    contentType = ContentType.ANY,
                    method = Method.GET,
                    path = "comments",
                    paramsKeys = {"postId" },
                    paramsValues = {"2" },
                    headersKeys = {"Content-Type" },
                    headersValues = {"application/json" },
                    value = JsonObject.class
            )
    })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls")
    public void testRestCalls(Map<Integer, ResponseObject<JsonObject>> responseObject) {
        log.info("" + responseObject.get(1).getResponseToObject());
    }
}
