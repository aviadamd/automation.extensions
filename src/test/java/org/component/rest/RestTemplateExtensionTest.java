package org.component.rest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import org.utils.rest.restTemplate.RestTemplateCollector;
import org.utils.rest.restTemplate.RestTemplateExtension;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RestTemplateExtensionTest {

    @Test
    void restTemplateDemo() {
        RestTemplateExtension restTemplateExtension = new RestTemplateExtension();

        UriComponentsBuilder uriComponentsBuilder = restTemplateExtension
                .uriBuilder("https", "jsonplaceholders.typicode.com")
                .path("comments")
                .uriVariables(Map.of("postId","2"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        RestTemplateCollector<String> restTemplateCollector = restTemplateExtension.get(uriComponentsBuilder, httpEntity, String.class);

        if (restTemplateCollector.isPassResponse()) {
            log.info(restTemplateCollector.getResponseEntity().getBody());
        } else {
            if (restTemplateCollector.getRestClientResponseException() != null) {
                log.error(restTemplateCollector.getRestClientResponseException().getMessage());
                log.error(restTemplateCollector.getRestClientResponseException().getStatusText());
                log.error(restTemplateCollector.getRestClientResponseException().getResponseBodyAsString());
            } else {
                log.error(restTemplateCollector.getGeneralException().getMessage());
            }
        }
    }
}
