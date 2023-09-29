package org.utils.rest.assured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Slf4j
 public class RestAssuredBuilder {

    private String baseUri = "";
    private String setPath = "";
    private Map<String,String> setBody = new HashMap<>();
    private ContentType setContentType = ContentType.ANY;
    private Map<String,String> setQueryParams = new HashMap<>();
    private Map<String,String> setHeaders = new HashMap<>();

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public void setPath(String setPath) {
        this.setPath = setPath;
    }

    public void setContentType(ContentType setContentType) {
        this.setContentType = setContentType;
    }

    public void setHeaders(Map<String,String> setHeaders) {
        this.setHeaders = setHeaders;
    }

    public void setBody(Map<String,String> setBody) {
        this.setBody = setBody;
    }

    public void setQueryParams(Map<String,String> setQueryParams) {
        this.setQueryParams = setQueryParams;
    }

    public Response build(Method method) {
        try {

            RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

            requestSpecBuilder
                    .setBaseUri(this.baseUri)
                    .setBasePath(this.setPath)
                    .setContentType(this.setContentType);

            if (!this.setQueryParams.isEmpty()) requestSpecBuilder.addQueryParams(this.setQueryParams);
            if (!this.setHeaders.isEmpty()) requestSpecBuilder.addHeaders(this.setHeaders);
            if (!this.setBody.isEmpty()) requestSpecBuilder.setBody(this.setBody);

            return RestAssured
                    .given()
                    .spec(requestSpecBuilder.build())
                    .request(method);

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            this.resetFields();
        }
    }

    public void resetFields() {
        this.baseUri = "";
        this.setPath = "";
        this.setContentType = ContentType.ANY;
        this.setQueryParams = new HashMap<>();
        this.setHeaders = new HashMap<>();
        this.setBody = new HashMap<>();
    }

}
