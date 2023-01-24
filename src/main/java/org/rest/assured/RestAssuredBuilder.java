package org.rest.assured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class RestAssuredBuilder {
    public static class RequestBuilder {
        private String baseUri = "";
        private String setPath = "";
        private Map<String,String> setBody = new HashMap<>();
        private ContentType setContentType = null;
        private Map<String,String> setQueryParams = new HashMap<>();
        private Map<String,String> setHeaders = new HashMap<>();

        public RequestBuilder setBaseUri(String baseUri) {
            this.baseUri = baseUri;
            return this;
        }
        public RequestBuilder setPath(String setPath) {
            this.setPath = setPath;
            return this;
        }
        public RequestBuilder setContentType(ContentType setContentType) {
            this.setContentType = setContentType;
            return this;
        }

        public RequestBuilder setHeaders(Map<String,String> setHeaders) {
            this.setHeaders = setHeaders;
            return this;
        }

        public RequestBuilder setBody(Map<String,String> setBody) {
            this.setBody = setBody;
            return this;
        }

        public RequestBuilder setQueryParams(Map<String,String> setQueryParams) {
            this.setQueryParams = setQueryParams;
            return this;
        }

        public Response getResponse(Method method) {
            RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
            requestSpecBuilder.setBaseUri(this.baseUri);

            if (!this.setPath.isEmpty()) requestSpecBuilder.setBasePath(this.setPath);
            if (this.setContentType != null && !this.setContentType.toString().isEmpty()) requestSpecBuilder.setContentType(this.setContentType);
            if (!this.setQueryParams.isEmpty()) requestSpecBuilder.addQueryParams(this.setQueryParams);
            if (!this.setHeaders.isEmpty()) requestSpecBuilder.addHeaders(this.setHeaders);
            if (!this.setBody.isEmpty()) requestSpecBuilder.setBody(this.setBody);

            return RestAssured.given().spec(requestSpecBuilder.build()).request(method);
        }
    }
    public JsonPath getJsonPath(Response response) {
        return response.then().extract().jsonPath();
    }
}
