package org.extensions.rest;

import io.restassured.response.Response;
import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.extension.*;
import org.utils.rest.assured.ResponseCollector;
import org.utils.rest.assured.RestAssuredBuilder;
import java.util.HashMap;

public class RestAssuredBuilderExtension implements BeforeEachCallback, ParameterResolver {
    private final ThreadLocal<ResponseCollector> responseCollector = new ThreadLocal<>();

    @Override
    public synchronized boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == ResponseCollector.class;
    }

    @Override
    public synchronized Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            return this.responseCollector.get();
        } else throw new RuntimeException("fail init rest calls " );
    }

    @Override
    public synchronized void beforeEach(ExtensionContext context)  {
        if (context.getElement().isPresent()) {

            RestDataProvider provider = context.getElement().get().getAnnotation(RestDataProvider.class);
            for (RestStep stepProvider: provider.restSteps()) {

                HashMap<String, String> queryParams = this.arrayToMap(stepProvider.paramsKeys(), stepProvider.paramsValues());
                HashMap<String, String> headerParams = this.arrayToMap(stepProvider.headersKeys(), stepProvider.headersValues());
                HashMap<String, String> bodyParams = this.arrayToMap(stepProvider.bodyKeys(), stepProvider.bodyValues());

                RestAssuredBuilder assuredBuilder = new RestAssuredBuilder();
                assuredBuilder.setBaseUri(provider.basePath()).setPath(stepProvider.urlPath());
                if (stepProvider.contentType() != null) assuredBuilder.setContentType(stepProvider.contentType());
                if (queryParams.size() > 0) assuredBuilder.setQueryParams(queryParams);
                if (headerParams.size() > 0) assuredBuilder.setHeaders(headerParams);
                if (bodyParams.size() > 0) assuredBuilder.setBody(bodyParams);

                HashMap<Integer, Response> responseHashMap = new HashMap<>();
                responseHashMap.put(stepProvider.stepId(), assuredBuilder.build(stepProvider.method()));
                ResponseCollector collector = new ResponseCollector(responseHashMap);
                this.responseCollector.set(collector);
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
}
