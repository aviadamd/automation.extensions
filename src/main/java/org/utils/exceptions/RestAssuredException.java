package org.utils.exceptions;

import com.aventstack.extentreports.Status;
import org.extensions.report.Category;
import org.extensions.report.TestData;
import org.extensions.rest.RestAssuredError;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;
import org.utils.TestDataObserverBus;
import java.nio.charset.Charset;

public class RestAssuredException extends RestClientResponseException {

    /**
     * Construct a new instance of with the given response data.
     * @param message RestClientResponseException message
     * @param statusCode      the raw status code value
     * @param statusText      the status text
     * @param responseHeaders the response headers (can be {@code null})
     * @param responseBody    the response body content (can be {@code null})
     * @param responseCharset the response body charset (can be {@code null})
     */
    public RestAssuredException(
            String message,
            int statusCode,
            String statusText,
            HttpHeaders responseHeaders,
            byte[] responseBody,
            Charset responseCharset) {

        super(message, statusCode, statusText, responseHeaders, responseBody, responseCharset);

        RestAssuredError restAssuredError = new RestAssuredError(
                "",
                super.getRawStatusCode(),
                super.getResponseHeaders() != null ? super.getResponseHeaders().toSingleValueMap().toString() : "",
                super.getResponseBodyAsString()
        );

        TestDataObserverBus.onNext(new TestData<>(Status.FAIL, Category.REST, restAssuredError, RestAssuredException.class));
    }

}
