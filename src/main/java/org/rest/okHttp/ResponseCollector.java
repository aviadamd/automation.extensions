package org.rest.okHttp;

import okhttp3.Response;

public class ResponseCollector {
    public boolean passRequest;

    public String exception;
    public Response response;
    public ResponseData responseData;

    public ResponseCollector(boolean passRequest, Response response, String exception) {
        this.passRequest = passRequest;
        this.response = response;
        this.exception = exception;
        this.responseData = new ResponseData(response);
    }

    public boolean isPassRequest() { return passRequest; }
    public String getException() { return exception; }
    public ResponseData getResponseData() { return responseData; }

}
