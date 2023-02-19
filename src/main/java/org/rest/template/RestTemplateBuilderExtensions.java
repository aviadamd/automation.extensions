package org.rest.template;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

public class RestTemplateBuilderExtensions {
    private HttpHeaders headers;
    private Map<String, String> params;
    private final RestTemplate restTemplate;
    private UriComponentsBuilder uriComponentsBuilder;
    public RestTemplateBuilderExtensions() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.params = new HashMap<>();
        this.uriComponentsBuilder = UriComponentsBuilder.newInstance();
    }

    public RestTemplateBuilderExtensions setScheme(String scheme) {
        this.uriComponentsBuilder.scheme(scheme);
        return this;
    }

    public RestTemplateBuilderExtensions setHost(String host) {
        this.uriComponentsBuilder.host(host);
        return this;
    }

    public RestTemplateBuilderExtensions setPath(String path) {
        this.uriComponentsBuilder.path(path);
        return this;
    }
    public RestTemplateBuilderExtensions setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public RestTemplateBuilderExtensions setHeaders(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }
    public ResponseEntity<String> build(HttpMethod method) {
        HttpEntity<?> entity = new HttpEntity<>(this.headers);
        this.headers = new HttpHeaders();

        String uriComponentsBuilder = this.uriComponentsBuilder.toUriString();
        this.uriComponentsBuilder = UriComponentsBuilder.newInstance();

        Map<String,String> params = this.params;
        this.params = new HashMap<>();

        return this.restTemplate.exchange(uriComponentsBuilder, method, entity, String.class, params);
    }


    public RestTemplateBuilderExtensions build() {
        return this;
    }
}
