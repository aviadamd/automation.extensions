package org.component.rest.assured.footballData.team;

import io.restassured.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.rest.RestDataBaseClassProvider;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.extensions.rest.ResponseCollectorRepo;
import org.extensions.rest.RestAssuredBuilderExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(value = { RestAssuredBuilderExtension.class })
@ReportSetUp(mongoDbName = "RestAssuredExtensionTest")
@RestDataBaseClassProvider(jsonPath = "footballSpec.json")
public class FootBallTeamsTests {

    @Test
    @Order(1)
    @RestDataProvider(restSteps = {
            @RestStep(
                    stepId = 1,
                    urlPath = "v4/teams/91",
                    expectedStatusCode = 200,
                    requestMethod = Method.GET
            )
    })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls1")
    void getMatches(ResponseCollectorRepo responseCollectorRepo) {
        responseCollectorRepo
                .findById(1)
                .statusCode(200);
    }
}
