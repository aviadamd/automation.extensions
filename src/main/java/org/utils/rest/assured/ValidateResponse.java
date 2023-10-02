package org.utils.rest.assured;

import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.*;
import io.restassured.specification.Argument;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class ValidateResponse {
    private final Response response;

    public ValidateResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }

    public ValidateResponse statusCode(Matcher<? super Integer> expectedStatusCode) {
        this.response.then()
                .assertThat()
                .statusCode(expectedStatusCode);
        return this;
    }

    public ValidateResponse statusCode(int expectedStatusCode) {
        this.response.then()
                .assertThat()
                .statusCode(expectedStatusCode);
        return this;
    }

    public ValidateResponse body(Matcher<?> matcher, Matcher<?>... additionalMatchers) {
        this.response.then()
                .assertThat()
                .body(matcher, additionalMatchers);
        return this;
    }

    public ValidateResponse body(String path, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then()
                .body(path, matcher, additionalKeyMatcherPairs);
        return this;
    }

    public ValidateResponse body(String path, List<Argument> arguments, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then()
                .assertThat()
                .body(path, arguments, matcher, additionalKeyMatcherPairs);
        return this;
    }

    public ValidateResponse body(List<Argument> arguments, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.response.then()
                .assertThat()
                .body(arguments, matcher, additionalKeyMatcherPairs);
        return this;
    }

    public ValidateResponse body(List<Argument> arguments, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then()
                .assertThat()
                .body(arguments, responseAwareMatcher);
        return this;
    }

    public ValidateResponse body(String path, List<Argument> arguments, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then()
                .assertThat()
                .body(path, arguments, responseAwareMatcher);
        return this;
    }

    public ValidateResponse body(String path, ResponseAwareMatcher<Response> responseAwareMatcher) {
        this.response.then()
                .assertThat()
                .body(path, responseAwareMatcher);
        return this;
    }
}
