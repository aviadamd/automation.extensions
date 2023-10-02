package org.extensions.rest;

import io.restassured.RestAssured;
        import io.restassured.response.Response;
        import io.restassured.specification.RequestSpecification;
        import io.swagger.parser.OpenAPIParser;
        import io.swagger.v3.oas.models.OpenAPI;
        import io.swagger.v3.oas.models.Operation;
        import io.swagger.v3.oas.models.PathItem;
        import io.swagger.v3.oas.models.Paths;
        import org.junit.Before;
        import org.junit.Test;

        import java.io.File;
        import java.util.Map;

        import static io.restassured.RestAssured.given;

public class GeneratedAPITest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://api.example.com"; // Set your API base URI
    }

    @Test
    public void generateAPITestsFromSwagger() {
        File swaggerFile = new File("path/to/api_swagger.json"); // Replace with your Swagger file path

        OpenAPI openAPI = new OpenAPIParser().readLocation(swaggerFile.getAbsolutePath(), null, null).getOpenAPI();
        Paths paths = openAPI.getPaths();

        paths.forEach((path, pathItem) -> {
            for (Map.Entry<PathItem.HttpMethod, Operation> operationEntry : pathItem.readOperationsMap().entrySet()) {
                PathItem.HttpMethod httpMethod = operationEntry.getKey();
                Operation operation = operationEntry.getValue();

                // Generate the test method name
                String testName = String.format("test%s%s", httpMethod.toString(), path.replaceAll("/", "_"));

                // Generate the test
                generateTest(httpMethod, path, operation);
            }
        });
    }

    private void generateTest(PathItem.HttpMethod httpMethod, String path, Operation operation) {
        Response response = this.constructRequest(httpMethod, path, operation)
                .when()
                .request(httpMethod.toString(), path)
                .then()
                .extract()
                .response();
    }

    private RequestSpecification constructRequest(PathItem.HttpMethod httpMethod, String path, Operation operation) {
        RequestSpecification requestSpec = given()
                .baseUri(RestAssured.baseURI)
                .basePath(path);

        if (operation.getParameters() != null) {
            operation.getParameters().forEach(parameter -> {
                if (parameter.getIn().equalsIgnoreCase("header")) {
                    requestSpec.header(parameter.getName(), "yourHeaderValue");
                }
            });
        }

        if (httpMethod == PathItem.HttpMethod.POST || httpMethod == PathItem.HttpMethod.PUT) {
            requestSpec.body("yourRequestBody");
        }

        return requestSpec;
    }

}

