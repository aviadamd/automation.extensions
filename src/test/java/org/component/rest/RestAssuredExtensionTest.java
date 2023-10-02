package org.component.rest;

import com.aventstack.extentreports.AnalysisStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.rest.RestDataBaseClassProvider;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.rest.RestStep;
import org.extensions.report.ExtentReportExtension;
import org.extensions.rest.ResponseCollectorRepo;
import org.extensions.rest.RestAssuredBuilderExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(value = { ExtentReportExtension.class, RestAssuredBuilderExtension.class })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST)
@RestDataBaseClassProvider(
        scheme = "https",
        basePath = "api.football-data.org",
        headersKeys = { "X-Auth-Token" },
        headersValues = { "8df69ce914ac49e5a64a485ad355ef56"})
public class RestAssuredExtensionTest {

    @Test
    @Order(1)
    @RestDataProvider(restSteps = {
            @RestStep(
                    stepId = 1,
                    urlPath = "v4/areas",
                    expectedStatusCode = 200,
                    contentType = ContentType.ANY, requestMethod = Method.POST,
                    headersKeys = { "Content-Type","X-Response-Control" }, headersValues = { "application/json","minified" }
            ),
            @RestStep(
                    stepId = 2,
                    urlPath = "v4/areas",
                    expectedStatusCode = 200,
                    contentType = ContentType.ANY, requestMethod = Method.GET,
                    headersKeys = { "Content-Type","X-Response-Control" }, headersValues = { "application/json","minified" }
            )
    })
    @Timeout(value = 1, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls1")
    void testRestCalls1(ResponseCollectorRepo responseCollectorRepo) {
        responseCollectorRepo
                .findByStepId(1)
                .statusCode(200);

        responseCollectorRepo
                .findByStepId(2)
                .statusCode(200);
    }

    @Test
    @Order(2)
    @RestDataProvider(restSteps = {
            @RestStep(
                    stepId = 1,
                    urlPath = "v4/areas",
                    expectedStatusCode = 200,
                    contentType = ContentType.ANY, requestMethod = Method.GET,
                    headersKeys = { "Content-Type","X-Response-Control" }, headersValues = { "application/json","minified" }
            )
    })
    @Timeout(value = 1, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls12")
    void testRestCalls2(ResponseCollectorRepo responseCollectorRepo) throws IOException {
        responseCollectorRepo
                .findByStepId(1)
                .statusCode(200)
                .body("areas.id", response -> Matchers.hasItem(2000))
                .body("areas.name", response -> Matchers.hasItems("Afghanistan"))
                .body("areas.name", response -> Matchers.hasItems("Angola"));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseCollectorRepo
                .findByStepId(1)
                .getResponse()
                .body()
                .prettyPrint());

        jsonNode.findValue("areas").findValues("name").forEach(System.out::println);
    }

}
