package org.utils.rest.assured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.Map;

public class RestAssuredHandler {

    private final RestAssuredGenericHandler restAssuredGenericHandler;

    public RestAssuredHandler(RestAssuredLoggingBuilder restAssuredLoggingBuilder) {
        this.restAssuredGenericHandler = new RestAssuredGenericHandler(restAssuredLoggingBuilder);
    }

    public RestAssuredValidateResponse validateResponse(Response response) {
        return new RestAssuredValidateResponse(response);
    }

    public Response get(String urlPath) {
        return this.execute(Method.GET, urlPath, new RequestSpecBuilder());
    }

    public Response get(String urlPath, Map<String, ?> parametersMap) {
        return this.execute(Method.GET, urlPath, new RequestSpecBuilder().addQueryParams(parametersMap));
    }

    public Response get(String urlPath, Map<String, ?> parametersMap, Map<String, String> headers) {
        return this.execute(Method.GET, urlPath, new RequestSpecBuilder()
                .addQueryParams(parametersMap)
                .addHeaders(headers));
    }

    public Response execute(Method method, String urlPath, RequestSpecBuilder requestSpecBuilder) {
        return this.restAssuredGenericHandler.execute(method, urlPath, requestSpecBuilder);
    }

}
