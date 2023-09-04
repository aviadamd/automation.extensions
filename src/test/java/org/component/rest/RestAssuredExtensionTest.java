package org.component.rest;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
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
import org.utils.rest.assured.RestAssuredResponseAdapter;

import java.util.HashMap;
import java.util.function.Predicate;

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
                    @RestStep(
                            stepId = 1,
                            contentType = ContentType.ANY,
                            method = Method.GET,
                            urlPath = "comments",
                            paramsKeys = { "postId" }, paramsValues = { "2" },
                            headersKeys = { "Content-Type" }, headersValues = { "application/json" },
                            responseStatusCode = 200
                    ),
                    @RestStep(
                            stepId = 2,
                            contentType = ContentType.ANY,
                            method = Method.GET, urlPath = "comments",
                            paramsKeys = { "postId" }, paramsValues = { "2" },
                            headersKeys = { "Content-Type" }, headersValues = { "application/json" },
                            responseStatusCode = 200, receiveHeadersKeys = "Etag"
                    )
            })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "testRestCalls")
    void testRestCalls(HashMap<Integer, Response> responseCollector) {
        RestAssuredResponseAdapter restAssuredResponseAdapter = new RestAssuredResponseAdapter();
        restAssuredResponseAdapter.setAssertionLevel(AssertionsLevel.HARD_AFTER_TEST);

        Response response = responseCollector.get(1);
        JsonNode jsonNode = restAssuredResponseAdapter.as(response, JsonNode.class, ObjectMapperType.JACKSON_2);
        restAssuredResponseAdapter.assertThat(jsonNode).isNotEmpty();
        restAssuredResponseAdapter.assertThat(jsonNode.findValue("id").asText()).isNotEmpty();
        restAssuredResponseAdapter.assertThat(jsonNode.findPath("postId")).isNotEmpty();

        Predicate<JsonPath> jsonPathPredicate = findBy -> !findBy.getString("id").isEmpty();
        restAssuredResponseAdapter.thatJsonPath(response, new Condition<>(jsonPathPredicate,""));
    }
}
