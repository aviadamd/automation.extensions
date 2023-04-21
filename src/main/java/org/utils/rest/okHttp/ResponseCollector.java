package org.utils.rest.okHttp;

import okhttp3.Response;

public class ResponseCollector {
    private boolean passRequest;
    private String exception;
    private ResponseData responseData;

    public void setPassRequest(boolean passRequest) {
        this.passRequest = passRequest;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setResponseData(Response response) {
        this.responseData = new ResponseData(response);
    }

    public boolean isPassRequest() { return passRequest; }
    public String getException() { return exception; }
    public ResponseData getResponseData() { return responseData; }


    @Override
    public String toString() {
        return "ResponseCollector{" +
                "passRequest=" + passRequest +
                ", exception='" + exception + '\'' +
                ", responseData=" + responseData +
                '}';
    }
}
