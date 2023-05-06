package org.component.rest;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
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
import org.utils.assertions.AssertionsLevel;
import org.utils.rest.assured.ResponseCollector;
import java.io.IOException;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { ExtentReportExtension.class, RestAssuredBuilderExtension.class })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST)
public class RestAssuredExtensionTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @RestDataProvider(basePath = "https://jsonplaceholder.typicode.com",
            restSteps = {
            @RestStep(stepId = 1, contentType = ContentType.ANY,
                    method = Method.GET, urlPath = "comments",
                    paramsKeys = { "postId" }, paramsValues = { "2" },
                    headersKeys = { "Content-Type" }, headersValues = { "application/json" }
            )
    })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls")
    void testRestCalls(ResponseCollector responseCollector) throws IOException {
        responseCollector.with().setAssertionLevel(AssertionsLevel.HARD_AFTER_TEST);

        Response response = responseCollector.responseMap().get(1);
        responseCollector.with().thatResponse(response).isNotNull();
        JsonNode jsonNode = responseCollector.with().as(response, JsonNode.class, ObjectMapperType.JACKSON_2);

        responseCollector.with().thatJsonNude(jsonNode).isNotEmpty();
        responseCollector.with().assertThat(jsonNode.findValue("id").asText()).isNotEmpty();
        responseCollector.with().thatResponseCode(response).isEqualByComparingTo(200);
        responseCollector.with().thatJsonNude(jsonNode.findPath("postId")).isNotEmpty();
    }
}
