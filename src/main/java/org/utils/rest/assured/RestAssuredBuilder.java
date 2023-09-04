 package org.utils.rest.assured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class RestAssuredBuilder {

    private String baseUri = "";
    private String setPath = "";
    private Map<String,String> setBody = new HashMap<>();
    private ContentType setContentType = ContentType.ANY;
    private Map<String,String> setQueryParams = new HashMap<>();
    private Map<String,String> setHeaders = new HashMap<>();

    public RestAssuredBuilder setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public RestAssuredBuilder setPath(String setPath) {
        this.setPath = setPath;
        return this;
    }

    public RestAssuredBuilder setContentType(ContentType setContentType) {
        this.setContentType = setContentType;
        return this;
    }

    public RestAssuredBuilder setHeaders(Map<String,String> setHeaders) {
        this.setHeaders = setHeaders;
        return this;
    }

    public RestAssuredBuilder setBody(Map<String,String> setBody) {
        this.setBody = setBody;
        return this;
    }

    public RestAssuredBuilder setQueryParams(Map<String,String> setQueryParams) {
        this.setQueryParams = setQueryParams;
        return this;
    }

    public Response build(Method method, boolean failOnExceptionCatch) {
        Response response = null;
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        try {

            requestSpecBuilder.setBaseUri(this.baseUri);
            requestSpecBuilder.setBasePath(this.setPath);
            requestSpecBuilder.setContentType(this.setContentType);
            if (!this.setQueryParams.isEmpty()) requestSpecBuilder.addQueryParams(this.setQueryParams);
            if (!this.setHeaders.isEmpty()) requestSpecBuilder.addHeaders(this.setHeaders);
            if (!this.setBody.isEmpty()) requestSpecBuilder.setBody(this.setBody);

            response = RestAssured.given().spec(requestSpecBuilder.build()).request(method);

        } catch (Exception exception) {
            if (failOnExceptionCatch) Assertions.fail(exception.getMessage(), exception);
        }

        return response;
    }
}
