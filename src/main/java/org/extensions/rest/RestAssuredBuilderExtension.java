package org.extensions.rest;

import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.extension.*;
import org.utils.rest.assured.RestAssuredBuilder;
import org.utils.rest.assured.RestAssuredResponseAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RestAssuredBuilderExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

    private RestAssuredResponseAdapter restAssuredResponseAdapter;
    private static final HashMap<String,String> headerParamsCollector = new HashMap<>();
    private static final HashMap<Integer, Response> responseCollector = new HashMap<>();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        this.restAssuredResponseAdapter = new RestAssuredResponseAdapter();
    }

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == HashMap.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            return responseCollector;
        } else throw new RuntimeException("fail init rest calls " );
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent() && context.getElement().get().isAnnotationPresent(RestDataProvider.class)) {

            RestDataProvider provider = context.getElement().get().getAnnotation(RestDataProvider.class);
            for (RestStep stepProvider : provider.restSteps()) {

                HashMap<String, String> queryParams = this.arrayToMap(stepProvider.paramsKeys(), stepProvider.paramsValues());
                HashMap<String, String> headerParams = this.arrayToMap(stepProvider.headersKeys(), stepProvider.headersValues());
                HashMap<String, String> bodyParams = this.arrayToMap(stepProvider.bodyKeys(), stepProvider.bodyValues());

                RestAssuredBuilder assuredBuilder = new RestAssuredBuilder();
                assuredBuilder.setBaseUri(provider.basePath());
                assuredBuilder.setPath(stepProvider.urlPath());
                assuredBuilder.setContentType(stepProvider.contentType());

                if (queryParams.size() > 0) assuredBuilder.setQueryParams(queryParams);

                if (headerParams.size() > 0) {
                    HashMap<String, String> headerCollector = this.passHeadersReceiver(stepProvider);
                    headerParams.putAll(headerCollector);
                    assuredBuilder.setHeaders(headerParams);
                }

                if (bodyParams.size() > 0) assuredBuilder.setBody(bodyParams);

                Response response = assuredBuilder.build(stepProvider.method(), false);
                this.restAssuredResponseAdapter.thatResponseCode(response).isEqualByComparingTo(stepProvider.responseStatusCode());
                this.headerCollector(response);
                responseCollector.put(stepProvider.stepId(), response);
            }
        }
    }

    private synchronized HashMap<String,String> arrayToMap(String[] first, String [] second) {
        HashMap<String, String> collector = new HashMap<>();
        for (int i = 0; i < first.length; i++) {
            collector.put(first[i], second[i]);
        }
        return collector;
    }

    private synchronized List<String> headersKeys(Response response) {
        return response.headers()
                .asList()
                .stream()
                .map(Header::getName)
                .collect(Collectors.toList());
    }
    private synchronized List<String> headersValues(Response response) {
        return response.headers()
                .asList()
                .stream()
                .map(Header::getValue)
                .collect(Collectors.toList());
    }

    private void headerCollector(Response response) {
        if (response.headers().size() > 0) {
            for (int header = 0; header < this.headersKeys(response).size(); header++) {
                headerParamsCollector.put(this.headersKeys(response).get(header), this.headersValues(response).get(header));
            }
        }
    }
    private synchronized HashMap<String,String> passHeadersReceiver(RestStep stepProvider) {
        HashMap<String, String> headerCollector = new HashMap<>();

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
}
