package org.utils.rest.assured;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ObjectAssert;
import org.jetbrains.annotations.NotNull;
import org.utils.assertions.AssertionsManager;
import java.lang.reflect.Type;

@Slf4j
public class RestAssuredResponseAdapter extends AssertionsManager {

    public IntegerAssert thatResponseCode(@NotNull Response response) { return super.assertThat(response.statusCode()); }
    public ObjectAssert<Response> thatResponse(@NotNull Response response) { return super.assertThat(response); }
    public IterableAssert<JsonNode> thatJsonNude(@NotNull JsonNode jsonNode) { return super.assertThat(jsonNode); }


    public <T> T as(@NotNull Response response, Class<T> tClass) {
        try {
            return response.as(tClass);
        } catch (Exception exception) {
            throw this.handleException(exception, tClass);
        }
    }

    public <T> T as(@NotNull Response response, Class<T> tClass, ObjectMapperType mapperType) {
        try {
            return response.as(tClass, mapperType);
        } catch (Exception exception) {
            throw this.handleException(exception, tClass);
        }
    }

    public <T> T as(@NotNull Response response, Type type) {
        try {
            return response.as(type);
        } catch (Exception exception) {
            throw this.handleException(exception, type);
        }
    }

    public <T> T as(@NotNull Response response, Type type, ObjectMapperType mapperType) {
        try {
            return response.as(type, mapperType);
        } catch (Exception exception) {
            throw this.handleException(exception, type);
        }
    }

    private RuntimeException handleException(Exception exception, Type type) {
        final String error = exception.getMessage();
        final String className = type.getTypeName();
        return new RuntimeException("response as " + className + " throws error " + error, exception);
    }

    private <T> RuntimeException handleException(Exception exception, Class<T> tClass) {
        final String error = exception.getMessage();
        final String className = tClass.getSimpleName();
        return new RuntimeException("response as " + className + " throws error " + error, exception);
    }
}
