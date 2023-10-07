package org.utils.rest.assured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.uri.Uri;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.Assert;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;
import static org.awaitility.Awaitility.with;

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
            RestAssuredConfig restAssuredConfig,
            RequestSpecBuilder requestSpecBuilder,
            Method method, int responseCode,
            ProxySpecification proxySpecification) {
       return this.build(
               singletonList(restAssuredConfig),
               requestSpecBuilder,
               method,
               responseCode,
               proxySpecification
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
            Method method,
            int responseCode,
            ProxySpecification proxySpecification) {

        try {

//            try (AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient()) {
//                Future<org.asynchttpclient.Response> responseFuture = asyncHttpClient.executeRequest(new RequestBuilder()
//                        .setUri(Uri.create(""))
//                        .setRequestTimeout(10)
//                        .setMethod("GET"));
//                responseFuture.get().getResponseBody();
//            }


            RequestSpecification requestSpecification = RestAssured.given()
                    .relaxedHTTPSValidation();

            if (proxySpecification != null) {
                requestSpecification.proxy(proxySpecification);
            }

            requestSpecification
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
