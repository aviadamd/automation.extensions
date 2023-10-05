package org.utils.rest.okHttp;

import okhttp3.Request;

import java.util.List;
import java.util.Map;

public class ResponseHandler {

    private String exception;
    private int code;
    private String responseBody;
    private Map<String, List<String>> headersMap;
    private Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setHeadersMap(Map<String, List<String>> headersMap) {
        this.headersMap = headersMap;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getException() {
        return exception;
    }

    public int getCode() {
        return code;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Map<String, List<String>> getHeadersMap() {
        return headersMap;
    }

    public String printPretty() {
        return "ResponseHandler Info {" +
                "\n request=" + request +
                "\n response code=" + code +
                "\n headersMap=" + headersMap +
                "\n responseBody='" + responseBody + '\'' +
                "\n exception='" + exception + '\'' +
                "\n" +
                '}';
    }
}
