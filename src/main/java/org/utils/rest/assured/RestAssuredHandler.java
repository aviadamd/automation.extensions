package org.utils.rest.assured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import static java.util.Collections.singletonList;

@Slf4j
 public class RestAssuredHandler {

    /**
     * build
     * @param restAssuredConfig
     * @param requestSpecBuilder
     * @param method
     * @param responseCode
     * @return Response
     */
    public synchronized Response build(
            RestAssuredConfig restAssuredConfig, RequestSpecBuilder requestSpecBuilder, Method method, int responseCode) {
       return this.build(
               singletonList(restAssuredConfig),
               requestSpecBuilder,
               method,
               responseCode
       );
    }

    /**
     * response
     * @param restAssuredConfigs
     * @param requestSpecBuilder
     * @param method
     * @param responseCode
     * @return Response
     */
    public synchronized Response build(
            List<RestAssuredConfig> restAssuredConfigs,
            RequestSpecBuilder requestSpecBuilder,
            Method method, int responseCode) {

        try {

            RequestSpecification requestSpecification = RestAssured.given()
                    .relaxedHTTPSValidation()
                    .spec(requestSpecBuilder.build())
                    .then()
                    .statusCode(responseCode)
                    .request();

            if (restAssuredConfigs != null) restAssuredConfigs.forEach(requestSpecification::config);

            return requestSpecification.request(method);

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
