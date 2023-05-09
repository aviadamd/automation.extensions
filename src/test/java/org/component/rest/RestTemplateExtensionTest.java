package org.component.rest;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.base.OptionalWrapper;
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

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

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
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseAdapter<String> responseAdapter = restTemplateExtension.get(uriComponentsBuilder, httpEntity, String.class);
        assertionsManager.setAssertionLevel(AssertionsLevel.HARD_AFTER_TEST);

        assertionsManager.assertThat(responseAdapter).is(new Condition<>(ResponseAdapter::isPassResponse,"response fail"));

        if (responseAdapter.isPassResponse()) {
            log.info(responseAdapter.getResponse().getBody());
        } else {
            if (responseAdapter.getResponseException() != null) {
                log.error(responseAdapter.getResponseException().getMessage());
                log.error(responseAdapter.getResponseException().getStatusText());
                log.error(responseAdapter.getResponseException().getResponseBodyAsString());
            } else {
                log.error(responseAdapter.getGeneralException().getMessage());
            }
        }
    }
}
