package org.utils.rest.assured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ResponseObject <T> {

    private T responseToObject;
    private final JsonPath jsonPath;
    private final Response response;

    public ResponseObject(Response response, JsonPath jsonPath) {
        this.jsonPath = jsonPath;
        this.response = response;
    }

    public void setResponseToObject(Class<T> responseToObject) { this.responseToObject = this.response.as(responseToObject); }
    public Response getResponse() { return this.response; }
    public T getResponseToObject() { return this.responseToObject; }
    public JsonPath getJsonPath() { return this.jsonPath; }
}
