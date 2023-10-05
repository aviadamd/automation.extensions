package org.extensions.rest;

public class RestAssuredError {

    private final String url;
    private final int statusCode;
    private final String headers;
    private final String body;

    public RestAssuredError(String url, int statusCode, String headers, String body) {
        this.url = url;
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
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
