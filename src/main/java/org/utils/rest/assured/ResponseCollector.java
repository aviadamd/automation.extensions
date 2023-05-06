package org.utils.rest.assured;

import io.restassured.response.Response;

import java.util.HashMap;

public class ResponseCollector {

    private final HashMap<Integer, Response> response;
    private final RestAssuredResponseAdapter restAssuredResponseAdapter;

    public ResponseCollector(HashMap<Integer, Response> response) {
        this.response = response;
        this.restAssuredResponseAdapter = new RestAssuredResponseAdapter();
    }

    public RestAssuredResponseAdapter with() { return this.restAssuredResponseAdapter; }

    public HashMap<Integer, Response> responseMap() {
        return this.response;
    }

}
