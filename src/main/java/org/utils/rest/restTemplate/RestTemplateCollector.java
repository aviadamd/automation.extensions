package org.utils.rest.restTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

public class RestTemplateCollector<T> {
    private boolean passResponse;
    private ResponseEntity<T> responseEntity;
    private RestClientResponseException restClientResponseException;

    private Exception generalException;


    public RestTemplateCollector() {}

    public void setPassResponse(boolean passResponse) {
        this.passResponse = passResponse;
    }
    public void setResponseEntity(ResponseEntity<T> responseEntity) { this.responseEntity = responseEntity; }

    public void setGeneralException(Exception generalException) { this.generalException = generalException; }
    public void setRestClientResponseException(RestClientResponseException restClientResponseException) { this.restClientResponseException = restClientResponseException; }

    public boolean isPassResponse() {
        return passResponse;
    }

    public ResponseEntity<T> getResponseEntity() {
        return responseEntity;
    }

    public Exception getGeneralException() { return generalException; }

    public RestClientResponseException getRestClientResponseException() {
        return restClientResponseException;
    }
}

