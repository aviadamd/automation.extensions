package org.extensions.rest;

import org.extensions.anontations.rest.RestDataProvider;
import org.extensions.anontations.rest.RestStep;
import org.junit.jupiter.api.extension.*;
import org.rest.assured.ResponseObject;
import org.rest.assured.RestAssuredBuilder;
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
        }
        if (context.getExecutionException().isPresent()) {
            throw new RuntimeException("fail init rest calls ", context.getExecutionException().get());
        } else throw new RuntimeException("fail init rest calls " );
    }

    @Override
    public void beforeEach(ExtensionContext context)  {
        if (context.getElement().isPresent()) {

            this.restAssuredBuilder.set(new RestAssuredBuilder());
            RestDataProvider provider = context.getElement().get().getAnnotation(RestDataProvider.class);

            for (RestStep stepProvider: provider.restStep()) {

                HashMap<String, String> queryParams = this.arrayToMap(stepProvider.paramsKeys(), stepProvider.paramsValues());
                HashMap<String, String> headerParams = this.arrayToMap(stepProvider.headersKeys(), stepProvider.headersValues());
                HashMap<String, String> bodyParams = this.arrayToMap(stepProvider.bodyKeys(), stepProvider.bodyValues());

                ResponseObject<?> responseObject = this.restAssuredBuilder.get()
                        .setBaseUri(provider.baseUri())
                        .setPath(stepProvider.path())
                        .setContentType(stepProvider.contentType())
                        .setQueryParams(queryParams)
                        .setHeaders(headerParams)
                        .setBody(bodyParams)
                        .build(stepProvider.method(), stepProvider.value());
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
