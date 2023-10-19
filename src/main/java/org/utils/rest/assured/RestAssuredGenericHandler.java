package org.utils.rest.assured;

import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.extensions.report.ExtentTestManager;

import java.util.List;

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
                    .spec(requestSpecBuilder
                            .setBaseUri(this.restAssuredLoggingBuilder.getBaseUri())
                            .setBasePath(urlPath)
                            .addFilters(List.of(
                                    this.restAssuredLoggingBuilder.getRequestLoggingFilter(),
                                    this.restAssuredLoggingBuilder.getResponseLoggingFilter(),
                                    this.restAssuredLoggingBuilder.getErrorLoggingFilter())
                            )
                            .build()
                    ).filter((requestSpec, responseSpec, filterContext) -> {
                        ExtentTestManager.log(Status.INFO, "uri " + requestSpec.getURI());
                        ExtentTestManager.log(Status.INFO, "content type" + requestSpec.getContentType());
                        ExtentTestManager.log(Status.INFO, "method " + requestSpec.getMethod());
                        if (requestSpec.getBody() != null) ExtentTestManager.log(Status.INFO, "" + requestSpec.getBody());

                        Response response = filterContext.next(requestSpec, responseSpec);

                        if (response != null) {
                            ExtentTestManager.logTable(Status.INFO, "headers response ", response.headers().asList());
                            ExtentTestManager.logJson(Status.INFO, "body response ", response.body().asPrettyString());
                        }

                        return response;
                    });

            this.restAssuredLoggingBuilder.getConfigs().forEach(requestSpecification::config);

            return requestSpecification
                    .then()
                    .request()
                    .request(method);

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
