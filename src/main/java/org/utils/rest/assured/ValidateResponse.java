package org.utils.rest.assured;

import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.*;
import io.restassured.specification.Argument;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import java.util.List;

@Slf4j
public class ValidateResponse {
    private final Response response;

    public ValidateResponse(Response response) {
        this.response = response;
    }

    public void statusCode(Matcher<? super Integer> expectedStatusCode) {
        this.response.then()
                .assertThat()
                .statusCode(expectedStatusCode);
    }

    public void statusCode(int expectedStatusCode) {
        this.response.then()
                .assertThat()
                .statusCode(expectedStatusCode);
    }

    public void body(Matcher<?> matcher, Matcher<?>... additionalMatchers) {
        this.response.then()
                .assertThat()
                .body(matcher, additionalMatchers);
    }

    public void body(String path, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then()
                .body(path, matcher, additionalKeyMatcherPairs);
    }

    public void body(String path, List<Argument> arguments, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then()
                .assertThat()
                .body(path, arguments, matcher, additionalKeyMatcherPairs);
    }

    public void body(List<Argument> arguments, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then()
                .assertThat()
                .body(arguments, matcher, additionalKeyMatcherPairs);
    }

    public void body(List<Argument> arguments, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then()
                .assertThat()
                .body(arguments, responseAwareMatcher);
    }

    public void body(String path, List<Argument> arguments, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then()
                .assertThat()
                .body(path, arguments, responseAwareMatcher);
    }

    public void body(String path, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then()
                .assertThat()
                .body(path, responseAwareMatcher);
    }
}
