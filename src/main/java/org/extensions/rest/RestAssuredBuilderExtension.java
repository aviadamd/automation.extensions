package org.extensions.rest;

import com.aventstack.extentreports.Status;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.*;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.*;
import org.extensions.anontations.rest.RestDataBaseClassProvider;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.extension.*;
import org.utils.TestDataObserverBus;
import org.utils.rest.assured.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class RestAssuredBuilderExtension implements
        BeforeEachCallback, AfterEachCallback,
        AfterAllCallback, TestWatcher, ParameterResolver {

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

            if (restDataBaseClassProvider != null && restDataProvider != null) {
                for (RestStep stepProvider: restDataProvider.restSteps()) {
                    if (stepProvider != null) {
                        Response response = this.request(restDataBaseClassProvider, stepProvider);
                        responseCollectorRepo.get().addResponse(stepProvider.stepId(), new RestAssuredValidateResponse(response));
                        responseCollectorRepo.get().findById(stepProvider.stepId()).statusCode(stepProvider.expectedStatusCode());
                        this.headerCollector(response);
                    }
                }
            }
        }
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable cause) {
        if (context.getElement().isPresent() && cause != null) {
            if (cause instanceof AssertionError) {
                AssertionError assertionError = (AssertionError) cause;
                TestDataObserverBus.onNext(new TestData<>(Status.FAIL, Category.REST, assertionError.getMessage()));
            } else if (cause instanceof JsonPathException) {
                JsonPathException jsonPathException = (JsonPathException) cause;
                TestDataObserverBus.onNext(new TestData<>(Status.FAIL, Category.REST, jsonPathException.getMessage()  ));
            }
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context)  {
        if (context.getElement().isPresent()) {
            headerParamsCollector = new ConcurrentHashMap<>();
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            responseCollectorRepo.get().deleteAll();
            responseCollectorRepo.remove();
        }
    }

    private synchronized Response request(RestDataBaseClassProvider baseClassProvider, RestStep stepProvider) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

        ConcurrentHashMap<String, String> headerCollector = new ConcurrentHashMap<>();
        RestAssuredSpecFileReader restAssuredSpecFileReader;

        if (!baseClassProvider.jsonPath().isEmpty()) {
            restAssuredSpecFileReader = new RestAssuredSpecFileReader(baseClassProvider.jsonPath());
            if (restAssuredSpecFileReader.getHeaders() != null) {
                headerCollector.putAll(restAssuredSpecFileReader.getHeaders());
            }
        } else {
            restAssuredSpecFileReader = new RestAssuredSpecFileReader();
            restAssuredSpecFileReader.setBaseUri(baseClassProvider.scheme() + "://" + baseClassProvider.basePath());
        }

        requestSpecBuilder.setBaseUri(restAssuredSpecFileReader.getBaseUri());
        requestSpecBuilder.setContentType(stepProvider.contentType());

        if (stepProvider.paramsKeys().length > 0 && stepProvider.paramsValues().length > 0) {
            ConcurrentHashMap<String,String> arrayToMap = this.arrayToMap(stepProvider.paramsKeys(), stepProvider.paramsValues());
            requestSpecBuilder.addQueryParams(arrayToMap);
        }

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

        RestAssuredLoggingBuilder loggingBuilder = RestAssuredLoggingBuilder
                .builder()
                .baseUri(restAssuredSpecFileReader.getBaseUri())
                .build();
        RestAssuredGenericHandler restAssuredHandler = new RestAssuredGenericHandler(loggingBuilder);
        return restAssuredHandler.execute(stepProvider.requestMethod(), stepProvider.urlPath(), requestSpecBuilder);
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
