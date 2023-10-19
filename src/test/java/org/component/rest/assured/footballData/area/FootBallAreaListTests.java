package org.component.rest.assured.footballData.area;

import io.restassured.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.rest.RestDataBaseClassProvider;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.anontations.rest.RestStep;
import org.extensions.report.ExtentReportExtension;
import org.extensions.rest.ResponseCollectorRepo;
import org.extensions.rest.RestAssuredBuilderExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

// scheme = "https",
// basePath = "api.football-data.org",
// headersKeys = { "X-Auth-Token" },
// headersValues = { "8df69ce914ac49e5a64a485ad355ef56"}

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(value = { ExtentReportExtension.class, RestAssuredBuilderExtension.class })
@ReportSetUp(mongoDbName = "RestAssuredExtensionTest")
@RestDataBaseClassProvider(jsonPath = "footballSpec.json")
public class FootBallAreaListTests {

    @Test
    @Order(1)
    @RestDataProvider(restSteps = {
            @RestStep(
                    stepId = 1,
                    expectedStatusCode = 200,
                    urlPath = "v4/areas",
                    requestMethod = Method.GET
            )
    })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls1")
    void getAreas(ResponseCollectorRepo responseCollectorRepo) {
        responseCollectorRepo
                .findById(1)
                .header("Transfer-Encoding", "chunked")
                .header("Content-Type", "application/json;charset=UTF-8")
                .body("areas.id", response -> Matchers.hasItem(2000))
                .body("areas.name", response -> Matchers.hasItems("Afghanistan"))
                .body("areas.name", response -> Matchers.hasItems("Angola"));
    }

}
