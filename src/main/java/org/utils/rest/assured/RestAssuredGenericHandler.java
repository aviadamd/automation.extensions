package org.utils.rest.assured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

 public class RestAssuredGenericHandler {

    private final RestAssuredLoggingBuilder restAssuredLoggingBuilder;

     /**
      *  @param restAssuredLoggingBuilder
      *  baseUri most be initialized
      *  restAssuredConfigs optional
      *  requestLoggingFilter optional
      *  responseLoggingFilter optional
      *  errorLoggingFilter optional
      */
    public RestAssuredGenericHandler(RestAssuredLoggingBuilder restAssuredLoggingBuilder) {
        this.restAssuredLoggingBuilder = restAssuredLoggingBuilder;
    }

     /**
      * execute call
      * @param method
      *     GET,
      *     PUT,
      *     POST,
      *     DELETE,
      *     HEAD,
      *     TRACE,
      *     OPTIONS,
      *     PATCH
      * @param urlPath url path after the base path that will be configured in the restAssuredLoggingBuilder
      * @param requestSpecBuilder builder configuration
      * @return Response
      */
    public Response execute(Method method, String urlPath, RequestSpecBuilder requestSpecBuilder) {
        try {

            RequestSpecification requestSpecification = RestAssured.given()
                    .urlEncodingEnabled(true)
                    .relaxedHTTPSValidation()
                    .spec(requestSpecBuilder
                            .setBaseUri(this.restAssuredLoggingBuilder.getBaseUri())
                            .setBasePath(urlPath)
                            .addFilters(this.restAssuredLoggingBuilder.getFilters())
                            .build()
                    );

            this.restAssuredLoggingBuilder
                    .getConfigs()
                    .forEach(requestSpecification::config);

            return requestSpecification
                    .then()
                    .request()
                    .request(method);

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
