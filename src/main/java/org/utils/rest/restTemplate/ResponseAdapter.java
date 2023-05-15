package org.utils.rest.restTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

public class ResponseAdapter<T> {
    private boolean passResponse;
    private ResponseEntity<T> response;
    private int responseCode;
    private RestClientResponseException responseException;
    private Exception generalException;

    public ResponseAdapter() {}

    public void setPassResponse(boolean passResponse) { this.passResponse = passResponse; }
    public void setResponse(ResponseEntity<T> response) { this.response = response; }
    public void setGeneralException(Exception generalException) { this.generalException = generalException; }
    public void setResponseException(RestClientResponseException responseException) { this.responseException = responseException; }
    public void setResponseCode(int responseCode) { this.responseCode = responseCode; }
    public int getResponseCode() { return responseCode; }
    public boolean isPassResponse() {
        return passResponse;
    }
    public ResponseEntity<T> getResponse() { return response; }
    public Exception getGeneralException() { return generalException; }
    public RestClientResponseException getResponseException() {
        return responseException;
    }

}

