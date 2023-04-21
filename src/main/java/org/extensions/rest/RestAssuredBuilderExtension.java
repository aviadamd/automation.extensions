package org.extensions.rest;

import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.extension.*;
import org.utils.rest.assured.ResponseObject;
import org.utils.rest.assured.RestAssuredBuilder;
import java.util.HashMap;
import java.util.Map;

public class RestAssuredBuilderExtension implements BeforeEachCallback, ParameterResolver {
    private final ThreadLocal<RestAssuredBuilder> restAssuredBuilder = new ThreadLocal<>();
    private final ThreadLocal<Map<Integer, ResponseObject<?>>> responseMap = new ThreadLocal<>();

    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == ResponseObject.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        if (context.getElement().isPresent()) {
            return this.responseMap.get();
        } else throw new RuntimeException("fail init rest calls " );
    }

    @Override
    public void beforeEach(ExtensionContext context)  {
        if (context.getElement().isPresent()) {

            this.restAssuredBuilder.set(new RestAssuredBuilder());
            RestDataProvider provider = context.getElement().get().getAnnotation(RestDataProvider.class);

            for (RestStep stepProvider: provider.restSteps()) {

                HashMap<String, String> queryParams = this.arrayToMap(stepProvider.paramsKeys(), stepProvider.paramsValues());
                HashMap<String, String> headerParams = this.arrayToMap(stepProvider.headersKeys(), stepProvider.headersValues());
                HashMap<String, String> bodyParams = this.arrayToMap(stepProvider.bodyKeys(), stepProvider.bodyValues());

                RestAssuredBuilder assuredBuilder = this.restAssuredBuilder.get().setBaseUri(provider.baseUri()).setPath(stepProvider.path());

                if (stepProvider.contentType() != null) assuredBuilder.setContentType(stepProvider.contentType());
                if (queryParams.size() > 0) assuredBuilder.setQueryParams(queryParams);
                if (headerParams.size() > 0) assuredBuilder.setHeaders(headerParams);
                if (bodyParams.size() > 0) assuredBuilder.setBody(bodyParams);
                ResponseObject<?> responseObject = assuredBuilder.build(stepProvider.method(), stepProvider.value());
                this.responseMap.set(Map.of(stepProvider.stepId(), responseObject));
            }
        }
    }

    private HashMap<String,String> arrayToMap(String[] first, String [] second) {
        HashMap<String,String> collector = new HashMap<>();
        for (int i = 0; i < first.length; i++) {
            collector.put(first[i], second[i]);
        }
        return collector;
    }
}
