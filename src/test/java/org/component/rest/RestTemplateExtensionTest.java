package org.component.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Authenticator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.extensions.assertions.AssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import org.utils.assertions.AssertionsLevel;
import org.utils.assertions.AssertionsManager;
import org.utils.rest.restTemplate.ResponseAdapter;
import org.utils.rest.restTemplate.RestTemplateExtension;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ExtendWith(AssertionsExtension.class)
public class RestTemplateExtensionTest {

    @Test
    void restTemplateDemo(AssertionsManager assertionsManager) {
        RestTemplateExtension restTemplateExtension = new RestTemplateExtension();

        UriComponentsBuilder uriComponentsBuilder = restTemplateExtension
                .uriBuilder("https", "jsonplaceholder.typicode.com")
                .path("comments")
                .uriVariables(Map.of("postId","2"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseAdapter<JsonNode> responseAdapter = restTemplateExtension.get(uriComponentsBuilder, httpEntity, JsonNode.class);
        assertionsManager.setAssertionLevel(AssertionsLevel.HARD_AFTER_TEST);

        assertionsManager.assertThat(responseAdapter).is(new Condition<>(ResponseAdapter::isPassResponse,"response fail"));
        if (responseAdapter.isPassResponse()) {
            JsonNode body = responseAdapter.getResponse().getBody();
            if (body != null) {
                List<JsonNode> postsId = body.findValues("postId");
                log.info(postsId.toString());
            }

        } else {
            if (responseAdapter.getResponseException() != null) {
                log.error(responseAdapter.getResponseException().getMessage());
                log.error(responseAdapter.getResponseException().getStatusText());
                log.error(responseAdapter.getResponseException().getResponseBodyAsString());
                log.error(Objects.requireNonNull(responseAdapter.getResponseException().getResponseHeaders()).toString());
            } else {
                log.error(responseAdapter.getGeneralException().getMessage());
            }
        }
    }
}
