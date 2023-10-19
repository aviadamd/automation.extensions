package org.utils.rest.assured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Method;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import java.io.File;
import java.util.Map;

@Slf4j
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

    public Response post(String urlPath, Map<String, ?> parametersMap, Map<String, String> headers, JSONObject body) {
        return this.execute(Method.POST, urlPath, new RequestSpecBuilder()
                .addQueryParams(parametersMap)
                .addHeaders(headers)
                .setBody(body.toString()));
    }

    public Response post(String urlPath, Map<String, ?> parametersMap, Map<String, String> headers, Map<String,String> body) {
        return this.execute(Method.POST, urlPath, new RequestSpecBuilder()
                .addQueryParams(parametersMap)
                .addHeaders(headers)
                .setBody(body));
    }

    public Response post(String urlPath, Map<String, ?> parametersMap, Map<String, String> headers, File body) {
        return this.execute(Method.POST, urlPath, new RequestSpecBuilder()
                .addQueryParams(parametersMap)
                .addHeaders(headers)
                .setBody(body));
    }

    public Response execute(Method method, String urlPath, RequestSpecBuilder requestSpecBuilder) {
        return this.restAssuredGenericHandler.execute(method, urlPath, requestSpecBuilder);
    }

}
