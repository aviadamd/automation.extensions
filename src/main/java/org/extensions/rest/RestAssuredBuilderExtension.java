package org.extensions.rest;

import io.reactivex.rxjava3.core.Observable;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.base.ErrorsCollector;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.extension.*;
import org.utils.rest.assured.RestAssuredBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class RestAssuredBuilderExtension implements
        BeforeEachCallback, AfterEachCallback,
        TestWatcher, ParameterResolver {

    private static ConcurrentHashMap<Integer, Response> responseCollector = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> headerParamsCollector = new ConcurrentHashMap<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == ConcurrentHashMap.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            return responseCollector;
        } else throw new RuntimeException("RestAssuredBuilderExtension register fail init rest calls");
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            RestDataProvider provider = context.getElement().get().getAnnotation(RestDataProvider.class);
            if (provider != null) {
                for (RestStep stepProvider : provider.restSteps()) {
                    if (stepProvider != null) {
                        Response response = this.execute(provider, stepProvider);
                        this.responseCollector(stepProvider.stepId(), response);
                        this.headerCollector(response);
                        this.print(response);
                    }
                }
            }
        }
    }

    @Override
    public synchronized void testFailed(ExtensionContext context, Throwable throwable) {
        if (context.getElement().isPresent()) {
            String message = throwable.getClass().getSimpleName() + " has been catch in test, error: " + throwable.getMessage();
            Observable<String> observable = Observable.just(message);
            ErrorsCollector.getInstance().subscribeWithObserver(observable);
        }
    }

    @Override
    public synchronized void afterEach(ExtensionContext context)  {
        if (context.getElement().isPresent()) {
            responseCollector = new ConcurrentHashMap<>();
            headerParamsCollector = new ConcurrentHashMap<>();
        }
    }

    private synchronized Response execute(RestDataProvider provider, RestStep stepProvider) {
        ConcurrentHashMap<String, String> queryParams = this.arrayToMap(stepProvider.paramsKeys(), stepProvider.paramsValues());
        ConcurrentHashMap<String, String> headerParams = this.arrayToMap(stepProvider.headersKeys(), stepProvider.headersValues());
        ConcurrentHashMap<String, String> bodyParams = this.arrayToMap(stepProvider.bodyKeys(), stepProvider.bodyValues());

        RestAssuredBuilder assuredBuilder = new RestAssuredBuilder();

        assuredBuilder.setBaseUri(provider.basePath());
        assuredBuilder.setPath(stepProvider.urlPath());
        assuredBuilder.setContentType(stepProvider.contentType());

        if (queryParams.size() > 0) assuredBuilder.setQueryParams(queryParams);

        if (headerParams.size() > 0) {
            ConcurrentHashMap<String, String> headerCollector = this.passHeadersReceiver(stepProvider);
            headerParams.putAll(headerCollector);
            assuredBuilder.setHeaders(headerParams);
        }

        if (bodyParams.size() > 0) {
            assuredBuilder.setBody(bodyParams);
        }

        return assuredBuilder.build(stepProvider.method());
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

    private synchronized void responseCollector(int stepId, Response response) {
        responseCollector.put(stepId, response);
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

    private synchronized void print(Response response) {
        try {
            if (response != null) {
                if (response.headers() != null) log.info("response headers " + response.headers());
                if (response.statusCode() != 0) log.info("response status code " + response.statusCode());
                if (response.body() != null) log.info("response body " + response.getBody().asString());
            }
        } catch (Exception ignore) {}
    }
}
