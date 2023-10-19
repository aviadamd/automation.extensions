package org.utils.rest.assured;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RestAssuredSpecFileReader {

    private String baseUri;
    private HashMap<String, String> headers;

    public RestAssuredSpecFileReader() {}
    public RestAssuredSpecFileReader(String jsonPath) {
        JsonNode jsonNode = this.readApplicationJson(jsonPath);
        this.baseUri = this.readAndReturnBaseUrl(jsonNode);
        this.headers = this.readAndReturnHeaders(jsonNode);
    }

    public String getBaseUri() {
        return this.baseUri;
    }

    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    private String readAndReturnBaseUrl(JsonNode jsonNode) {
        return jsonNode.findValue("scheme")
                .asText()
                .concat("://")
                .concat(jsonNode.findValue("basePath").asText());
    }

    private HashMap<String,String> readAndReturnHeaders(JsonNode jsonNode) {
        List<String> keys = new ArrayList<>();
        HashMap<String, String> headersMap = new HashMap<>();
        Iterator<String> fieldNames = jsonNode.findValue("headers").fieldNames();
        fieldNames.forEachRemaining(keys::add);
        keys.forEach(key -> headersMap.put(key, jsonNode.findValue(key).asText()));
        return headersMap;
    }

    private JsonNode readApplicationJson(String jsonPath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String path = ClassLoader.getSystemResource(jsonPath).getPath();
            return objectMapper.readTree(new FileReader(path));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
