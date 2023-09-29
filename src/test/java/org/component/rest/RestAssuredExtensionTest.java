package org.component.rest;

import com.aventstack.extentreports.AnalysisStrategy;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.rest.RestStep;
import org.extensions.assertions.AssertJExtension;
import org.extensions.report.ExtentReportExtension;
import org.extensions.rest.RestAssuredBuilderExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.utils.assertions.AssertJCondition;
import org.utils.assertions.impl.AssertJJConditionImpl;
import org.utils.assertions.AssertJHandler;
import org.utils.assertions.impl.HamcrestJConditionImpl;
import java.util.concurrent.ConcurrentHashMap;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { ExtentReportExtension.class, AssertJExtension.class, RestAssuredBuilderExtension.class })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST)
public class RestAssuredExtensionTest {

    @Test
    @RestDataProvider(basePath = "https://jsonplaceholder.typicode.com",
            restSteps = {
                    @RestStep(
                            stepId = 1,
                            contentType = ContentType.ANY,
                            method = Method.GET,
                            urlPath = "comments",
                            paramsKeys = { "postId" }, paramsValues = { "2" },
                            headersKeys = { "Content-Type" }, headersValues = { "application/json" },
                            responseStatusCode = 200
                    )
            })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls")
    void testRestCalls(ConcurrentHashMap<Integer, Response> responseCollector) {
        AssertJHandler assertJHandler = new AssertJHandler();
        Response response = responseCollector.get(1);

        boolean verify = response.statusCode() == 200;

        AssertJCondition<Boolean> assertJCondition = new AssertJJConditionImpl<>(c -> verify, "response.statusCode() == 200");
        assertJHandler.is(true, assertJCondition);

        AssertJCondition<Boolean> hamcrestConditions = new HamcrestJConditionImpl<>(Matchers.is(verify), "response.statusCode() == 200");
        assertJHandler.is(true, hamcrestConditions);



    }
}
