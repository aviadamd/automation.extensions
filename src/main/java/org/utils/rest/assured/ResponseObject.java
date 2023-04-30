package org.utils.rest.assured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ResponseObject {

    private String responseToObject;
    private final JsonPath jsonPath;
    private final Response response;

    public ResponseObject(Response response, JsonPath jsonPath) {
        this.jsonPath = jsonPath;
        this.response = response;
    }

    public void setResponseToObject(Class<String> responseToObject) { this.responseToObject = this.response.as(responseToObject); }
    public Response getResponse() { return this.response; }
    public String getResponseToObject() { return this.responseToObject; }
    public JsonPath getJsonPath() { return this.jsonPath; }
}
