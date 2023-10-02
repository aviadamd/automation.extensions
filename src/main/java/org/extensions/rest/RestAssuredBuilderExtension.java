package org.extensions.rest;

import com.aventstack.extentreports.Status;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.FailureConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.base.RxJavaBus;
import org.base.TestDataTransfer;
import org.extensions.anontations.rest.RestDataBaseClassProvider;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.utils.rest.assured.RestAssuredHandler;
import org.utils.rest.assured.ValidateResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
public class RestAssuredBuilderExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    private static final ThreadLocal<ResponseCollectorRepo> responseCollectorRepo = new ThreadLocal<>();
    private static ConcurrentHashMap<String,String> headerParamsCollector = new ConcurrentHashMap<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        Class<ResponseCollectorRepo> responseCollectorRepoClass = ResponseCollectorRepo.class;
        return parameter.getParameter().getType() == responseCollectorRepoClass;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            return responseCollectorRepo.get();
        } else throw new RuntimeException("RestAssuredBuilderExtension register fail init rest calls");
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {

            responseCollectorRepo.set(new ResponseCollectorRepo());
            RestDataBaseClassProvider restDataBaseClassProvider = context.getRequiredTestClass().getAnnotation(RestDataBaseClassProvider.class);
            RestDataProvider restDataProvider = context.getElement().get().getAnnotation(RestDataProvider.class);

            Assertions.assertNotNull(restDataBaseClassProvider);
            Assertions.assertNotNull(restDataProvider);

            for (RestStep stepProvider : restDataProvider.restSteps()) {
                if (stepProvider != null) {
                    Response response = this.execute(restDataBaseClassProvider, stepProvider);
                    responseCollectorRepo.get().addResponse(stepProvider.stepId(), new ValidateResponse(response));
                    this.headerCollector(response);
                }
            }
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context)  {
        if (context.getElement().isPresent()) {
            headerParamsCollector = new ConcurrentHashMap<>();
            responseCollectorRepo.remove();
        }
    }

    private synchronized Response execute(RestDataBaseClassProvider baseClassProvider, RestStep stepProvider) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

        requestSpecBuilder
                .setBaseUri(baseClassProvider.scheme() + "://" + baseClassProvider.basePath())
                .setBasePath(stepProvider.urlPath())
                .setContentType(stepProvider.contentType());

        if (stepProvider.paramsKeys().length > 0 && stepProvider.paramsValues().length > 0) {
            ConcurrentHashMap<String,String> arrayToMap = this.arrayToMap(stepProvider.paramsKeys(), stepProvider.paramsValues());
            requestSpecBuilder.addQueryParams(arrayToMap);
        }

        ConcurrentHashMap<String, String> headerCollector = new ConcurrentHashMap<>();

        if (baseClassProvider.headersKeys().length > 0
                && baseClassProvider.headersValues().length > 0
                && baseClassProvider.headersKeys().length == baseClassProvider.headersValues().length) {
            ConcurrentHashMap<String, String> headersReceiver = this.headersReceiver(baseClassProvider.headersKeys(), baseClassProvider.headersValues());
            if (headersReceiver.size() > 0) {
                headerCollector.putAll(headersReceiver);
            }
        }

        if (stepProvider.headersKeys().length > 0
                && stepProvider.headersValues().length > 0
                && stepProvider.headersKeys().length == stepProvider.headersValues().length) {
            ConcurrentHashMap<String, String> headersReceiver = this.headersReceiver(stepProvider.headersKeys(), stepProvider.headersValues());
            if (headersReceiver.size() > 0) {
                headerCollector.putAll(headersReceiver);
            }
        }

        if (stepProvider.receiveHeadersKeys().length > 0) {
            ConcurrentHashMap<String, String> passHeadersReceiver = this.passHeadersReceiver(stepProvider);
            if (passHeadersReceiver.size() > 0) {
                headerCollector.putAll(passHeadersReceiver);
            }
        }

        if (headerCollector.size() > 0) {
            requestSpecBuilder.addHeaders(headerCollector);
        }

        if (stepProvider.bodyKeys().length > 0
                && stepProvider.bodyValues().length > 0
                && stepProvider.bodyKeys().length == stepProvider.bodyValues().length) {
            ConcurrentHashMap<String, String> bodyParams = this.arrayToMap(stepProvider.bodyKeys(), stepProvider.bodyValues());
            if (bodyParams.size() > 0) {
                requestSpecBuilder.setBody(bodyParams);
            }
        }

        final String url = baseClassProvider.scheme() + "://" + baseClassProvider.basePath() + "/" + stepProvider.urlPath();
        return new RestAssuredHandler().build(this.failureListener(url), requestSpecBuilder, stepProvider.requestMethod(), stepProvider.expectedStatusCode());
    }

    private synchronized RestAssuredConfig failureListener(String basePath) {
        return RestAssuredConfig.config()
                .failureConfig(FailureConfig
                .failureConfig()
                .with()
                .failureListeners((requestSpecification, responseSpecification, response) ->
                        RxJavaBus.publishFromObserver(new TestDataTransfer<>(
                                Status.FAIL,
                                "Rest",
                                "Error from " + basePath + "<br> with status code " + response.statusCode() + "<br> with body " + response.body().prettyPrint()
                ))));
    }

    private synchronized ConcurrentHashMap<String,String> arrayToMap(String[] first, String [] second) {
        ConcurrentHashMap<String, String> collector = new ConcurrentHashMap<>();
        for (int i = 0; i < first.length; i++) collector.put(first[i], second[i]);
        return collector;
    }

    private synchronized List<String> headersKeys(Response response) {
        return Collections.synchronizedList(response.headers()
                .asList()
                .stream()
                .map(Header::getName)
                .collect(Collectors.toList()));
    }
    private synchronized List<String> headersValues(Response response) {
        return Collections.synchronizedList(response.headers()
                .asList()
                .stream()
                .map(Header::getValue)
                .collect(Collectors.toList()));
    }

    private synchronized void headerCollector(Response response) {
        if (response != null && response.headers().size() > 0) {
            for (int header = 0; header < this.headersKeys(response).size(); header++) {
                headerParamsCollector.put(this.headersKeys(response).get(header), this.headersValues(response).get(header));
            }
        }
    }

    private synchronized ConcurrentHashMap<String,String> passHeadersReceiver(RestStep stepProvider) {
        ConcurrentHashMap<String, String> headerCollector = new ConcurrentHashMap<>();

        if (headerParamsCollector.size() > 0 && stepProvider.receiveHeadersKeys().length > 0) {
            for (int header = 0; header < stepProvider.receiveHeadersKeys().length; header++) {
                for (Map.Entry<String, String> entry : headerParamsCollector.entrySet()) {
                    if (entry.getKey().equals(stepProvider.receiveHeadersKeys()[header])) {
                        headerCollector.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        return headerCollector;
    }

    private synchronized ConcurrentHashMap<String,String> headersReceiver(String[] headersKeys, String[] headersValues) {
        ConcurrentHashMap<String, String> headerCollector = new ConcurrentHashMap<>();
        for (int header = 0; header < headersKeys.length; header++) {
            headerCollector.put(headersKeys[header], headersValues[header]);
        }
        return headerCollector;
    }
}
