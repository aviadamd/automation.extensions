package org.utils.rest.assured;

import com.aventstack.extentreports.Status;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.*;
import io.restassured.specification.Argument;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.extensions.report.ExtentTestManager;
import org.hamcrest.Matcher;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class RestAssuredValidateResponse {
    private final Response response;

    public RestAssuredValidateResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }

    public <T> T getResponseAs(Class<T> as) {
        return this.response.as(as);
    }

    public RestAssuredValidateResponse statusCode(Matcher<? super Integer> expectedStatusCode) {
        this.response.then().assertThat().statusCode(expectedStatusCode);
        ExtentTestManager.log(Status.INFO, "actual response code is: " + expectedStatusCode.matches(this.response.statusCode()));
        return this;
    }

    public RestAssuredValidateResponse statusCode(int expectedStatusCode) {
        this.response.then().assertThat().statusCode(expectedStatusCode);
        ExtentTestManager.log(Status.INFO, "actual response code is: " + expectedStatusCode);
        return this;
    }

    public RestAssuredValidateResponse header(String header, Matcher<?> expectedValueMatcher) {
        this.response.then().assertThat().header(header, expectedValueMatcher);
        return this;
    }

    public RestAssuredValidateResponse header(String headerName, String expectedValue) {
        this.response.then().assertThat().header(headerName, expectedValue);
        ExtentTestManager.log(Status.INFO, "header name key " + headerName + " match to " + expectedValue + " header value");
        return this;
    }

    /**
     *
     * @param consumer
     * @return
     */
    public RestAssuredValidateResponse body(Consumer<JsonAssert.ConfigurableJsonAssert> consumer) {
        consumer.accept(JsonAssertions.assertThatJson(response.body().asString()));
        return this;
    }

    /**
     *
     * @param matcher
     * @param additionalMatchers
     * @return
     */
    public RestAssuredValidateResponse body(Matcher<?> matcher, Matcher<?>... additionalMatchers) {
        this.response.then().assertThat().body(matcher, additionalMatchers);
        return this;
    }

    /**
     *
     * @param path
     * @param matcher
     * @param additionalKeyMatcherPairs
     * @return
     */
    public RestAssuredValidateResponse body(String path, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then().body(path, matcher, additionalKeyMatcherPairs);
        return this;
    }

    /**
     *
     * @param path
     * @param arguments
     * @param matcher
     * @param additionalKeyMatcherPairs
     * @return
     */
    public RestAssuredValidateResponse body(String path, List<Argument> arguments, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then().assertThat().body(path, arguments, matcher, additionalKeyMatcherPairs);
        return this;
    }

    /**
     *
     * @param arguments
     * @param matcher
     * @param additionalKeyMatcherPairs
     * @return
     */
    public RestAssuredValidateResponse body(List<Argument> arguments, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then().assertThat().body(arguments, matcher, additionalKeyMatcherPairs);
        return this;
    }

    /**
     *
     * @param arguments
     * @param responseAwareMatcher
     * @return
     */
    public RestAssuredValidateResponse body(List<Argument> arguments, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then().assertThat().body(arguments, responseAwareMatcher);
        return this;
    }

    /**
     *
     * @param path
     * @param arguments
     * @param responseAwareMatcher
     * @return
     */
    public RestAssuredValidateResponse body(String path, List<Argument> arguments, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then().assertThat().body(path, arguments, responseAwareMatcher);
        return this;
    }

    /**
     *
     * @param path
     * @param responseAwareMatcher
     * @return
     */
    public RestAssuredValidateResponse body(String path, ResponseAwareMatcher<Response> responseAwareMatcher) {
        try {
            this.response.then().assertThat().body(path, responseAwareMatcher);
            log.info("path " + path + " body is " + responseAwareMatcher.matcher(this.response));
            ExtentTestManager.log(Status.INFO, "path " + path + " body is " + responseAwareMatcher.matcher(this.response));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return this;
    }
}
