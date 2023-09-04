package org.component.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ListAssert;
import org.extensions.assertions.AssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import org.utils.assertions.AssertCondition;
import org.utils.assertions.AssertionsLevel;
import org.utils.assertions.AssertionsManager;
import org.utils.rest.restTemplate.ResponseAdapter;
import org.utils.rest.restTemplate.RestTemplateExtension;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
@ExtendWith(AssertionsExtension.class)
public class RestTemplateExtensionTest {

    @Test
    void restTemplateDemo(AssertionsManager assertionsManager) {
        assertionsManager.setAssertionLevel(AssertionsLevel.SOFT);

        AssertCondition<List<String>> assertCondition = new AssertCondition<>(list -> list.size() > 1 && list.contains("1"), "contains 1 and have more than 1 element");
        assertionsManager.is(List.of("2","1"), assertCondition);

        Predicate<List<String>> hasItems = findBy -> findBy.contains("1") || findBy.contains("2");
        assertCondition = new AssertCondition<>(hasItems,"");
        assertionsManager.is(List.of("2","1"), assertCondition);

        Predicate<ListAssert<String>> listAssertPredicate = stringListAssert -> stringListAssert.contains("1","2") != null;
        Condition<ListAssert<String>> assertConditions = new Condition<>(listAssertPredicate, "contains 1 and have more than 1 element");
        assertionsManager.assertThat(assertConditions).isNotNull();

        ListAssert<String> listAsserts = new ListAssert<>(List.of("1","1")).contains("1","1");
        assertionsManager.assertThat(listAsserts).isNotNull();

        RestTemplateExtension restTemplateExtension = new RestTemplateExtension();
        UriComponentsBuilder uriComponentsBuilder = restTemplateExtension
                .uriBuilder("https", "jsonplaceholder.typicode.com")
                .path("comments")
                .uriVariables(Map.of("postId","2"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseAdapter<JsonNode> responseAdapter = restTemplateExtension.get(uriComponentsBuilder, httpEntity, JsonNode.class);

        assertionsManager.assertThat(responseAdapter.getResponseCode())
                .isNotEqualTo(0)
                .as("response code error " + responseAdapter.getResponseCode())
                .isEqualTo(200);
        restTemplateExtension.printResponseExceptions(responseAdapter);
    }
}
