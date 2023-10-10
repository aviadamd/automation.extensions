package org.extensions.rest;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.FailureConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.*;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;
import org.extensions.report.*;
import org.extensions.anontations.rest.RestDataBaseClassProvider;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.extension.*;
import org.utils.TestDataObserverBus;
import org.utils.rest.assured.RestAssuredHandler;
import org.utils.rest.assured.ValidateResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class RestAssuredBuilderExtension extends ExtentReportExtension implements
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        TestWatcher,
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
    public synchronized void beforeAll(ExtensionContext context) {
        super.beforeAll(context);
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        super.beforeEach(context);

        if (context.getElement().isPresent()) {

            responseCollectorRepo.set(new ResponseCollectorRepo());
            RestDataBaseClassProvider restDataBaseClassProvider = context.getRequiredTestClass().getAnnotation(RestDataBaseClassProvider.class);
            RestDataProvider restDataProvider = context.getElement().get().getAnnotation(RestDataProvider.class);

            if (restDataBaseClassProvider != null && restDataProvider != null) {
                for (RestStep stepProvider: restDataProvider.restSteps()) {
                    if (stepProvider != null) {
                        Response response = this.request(restDataBaseClassProvider, stepProvider);
                        responseCollectorRepo.get().addResponse(stepProvider.stepId(), new ValidateResponse(response));
                        this.headerCollector(response);
                    }
                }
            }
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (context.getElement().isPresent() && cause != null) {
            if (cause instanceof AssertionError) {
                AssertionError assertionError = (AssertionError) cause;
                TestDataObserverBus.onNext(new TestData<>(Status.FAIL, Category.REST, assertionError.getMessage(), assertionError.getClass()));
            } else if (cause instanceof JsonPathException) {
                JsonPathException jsonPathException = (JsonPathException) cause;
                TestDataObserverBus.onNext(new TestData<>(Status.FAIL, Category.REST, jsonPathException.getMessage(), jsonPathException.getClass()));
            }
        }
        super.testFailed(context, cause);
    }

    @Override
    public synchronized void afterEach(ExtensionContext context)  {
        if (context.getElement().isPresent()) {
            headerParamsCollector = new ConcurrentHashMap<>();
        }
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {
        super.afterAll(context);
        if (context.getElement().isPresent()) {
            responseCollectorRepo.get().deleteAll();
            responseCollectorRepo.remove();
        }
    }

    private synchronized Response request(RestDataBaseClassProvider baseClassProvider, RestStep stepProvider) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

        String baseUri = "";
        ConcurrentHashMap<String, String> headerCollector = new ConcurrentHashMap<>();

        if (!baseClassProvider.jsonPath().isEmpty()) {
            try {

                JsonNode jsonNode = this.readApplicationJson(baseClassProvider.jsonPath());

                String scheme = jsonNode.findValue("scheme").asText();
                String basePath = jsonNode.findValue("basePath").asText();
                baseUri = scheme + "://" + basePath;

                List<String> keys = new ArrayList<>();
                Iterator<String> fieldNames = jsonNode.findValue("headers").fieldNames();
                fieldNames.forEachRemaining(keys::add);

                keys.forEach(key -> headerCollector.put(key, jsonNode.findValue(key).asText()));

            } catch (Exception ignore) {}

        } else baseUri = baseClassProvider.scheme() + "://" + baseClassProvider.basePath();

        requestSpecBuilder
                .setBaseUri(baseUri)
                .setBasePath(stepProvider.urlPath())
                .setContentType(stepProvider.contentType())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL, new PrintStream(new LogOutputStream() {
                    @Override
                    protected void processLine(String message, int i) {
                        log.debug(message);
                        ExtentTestManager.getInstance().log(Status.INFO, message);
                    }
                })))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL, new PrintStream(new LogOutputStream() {
                    @Override
                    protected void processLine(String message, int i) {
                        log.debug(message);
                    }
                })))
                .addFilter(new ErrorLoggingFilter(new PrintStream(new LogOutputStream() {
                    @Override
                    protected void processLine(String message, int i) {
                        log.error(message);
                        ExtentTestManager.getInstance().log(Status.FAIL, message);
                    }
                })));

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

        return new RestAssuredHandler().build(
                this.failureListener(baseUri + "/" + stepProvider.urlPath()),
                requestSpecBuilder,
                stepProvider.requestMethod(),
                stepProvider.expectedStatusCode(),
                null
        );
    }

    private synchronized RestAssuredConfig failureListener(String basePath) {
        return RestAssuredConfig.config()
                .failureConfig(FailureConfig.failureConfig()
                .with().failureListeners((requestSpecification, responseSpecification, response) -> {
                    RestAssuredError restAssuredError = new RestAssuredError(basePath, response.getStatusCode(), response.headers().toString(), response.body().prettyPrint());
                    TestDataObserverBus.onNext(new TestData<>(Status.FAIL, Category.REST, restAssuredError, Exception.class));
                }));
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

    private synchronized JsonNode readApplicationJson(String jsonPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String path = ClassLoader.getSystemResource(jsonPath).getPath();
        return objectMapper.readTree(new FileReader(path));
    }

    private synchronized ConcurrentHashMap<String,String> headersReceiver(String[] headersKeys, String[] headersValues) {
        ConcurrentHashMap<String, String> headerCollector = new ConcurrentHashMap<>();
        for (int header = 0; header < headersKeys.length; header++) {
            headerCollector.put(headersKeys[header], headersValues[header]);
        }
        return headerCollector;
    }

}
