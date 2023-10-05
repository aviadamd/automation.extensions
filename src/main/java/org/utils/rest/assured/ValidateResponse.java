package org.utils.rest.assured;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.*;
import io.restassured.specification.Argument;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import net.javacrumbs.jsonunit.assertj.JsonAssertion;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class ValidateResponse {
    private final Response response;

    public ValidateResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }

    public <T> T getResponseAs(Class<T> as) {
        return this.response.as(as);
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

    public ValidateResponse header(String header, Matcher<?> expectedValueMatcher) {
        this.response.then()
                .assertThat()
                .header(header, expectedValueMatcher);
        return this;
    }

    public ValidateResponse header(String headerName, String expectedValue) {
        this.response.then()
                .assertThat()
                .header(headerName, expectedValue);
        return this;
    }
    public ValidateResponse body(Consumer<JsonAssert.ConfigurableJsonAssert> consumer) {
        consumer.accept(JsonAssertions.assertThatJson(response.body().asString()));
        consumer.accept(JsonAssertions.assertThatJson(response.headers().asList()));
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
