package org.utils.rest.assured;

import io.restassured.response.Response;

public class RestAssuredData {
    private final String url;
    private final int statusCode;
    private final String headers;
    private final String body;

    public RestAssuredData(String url, Response response) {
        this.url = url;
        this.statusCode = response.statusCode();
        this.headers = response.headers().toString();
        this.body = response.body().prettyPrint();
    }

    @Override
    public String toString() {
        return "RestError {" +
                "\n <br> URL: " + url +
                "\n <br> CODE: " + statusCode +
                "\n <br> HEADERS: " + headers +
                "\n <br> BODY: " + body + "<br>\n }";
    }
}
