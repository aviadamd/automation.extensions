package org.component.assertions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.ConfigurationWhen;
import net.javacrumbs.jsonunit.fluent.JsonFluentAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.report.ExtentReportExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.IOException;
import static java.util.Collections.singletonMap;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static org.assertj.core.api.Assertions.entry;

@Slf4j
@ExtendWith(value = { ExtentReportExtension.class })
public class JsonAssertJExtensionProviderTest {

    @Test
    void objectDoesContainComplexValueError() {
        AssertionsForClassTypes.assertThatThrownBy(() -> JsonAssertions.assertThatJson("{\"root\":{\"a\":1, \"b\": {\"c\" :3}}}")
                .node("root")
                .isObject()
                .doesNotContainValue(("{\"c\" :3}")))
                .hasMessage(
                        """
                        [Different value found in node "root"]\s
                        Expecting:
                          <{"a":1,"b":{"c":3}}>
                        not to contain value:
                          <{"c":3}>
                          """
                );
    }

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    void jsonAssertionsExtensionTest() {
        JsonFluentAssert.assertThatJson(jsonTreeNode("{\"test\":1.00}")).node("test").withTolerance(0).isEqualTo(1);

        JsonFluentAssert.assertThatJson(jsonTreeObject("{\"test\":1.00}")).node("test").withTolerance(0).isEqualTo(2);

        JsonFluentAssert.assertThatJson("{\"root\":{\"test\":[1,2,3]}}")
                .node("root.test[-1]")
                .isEqualTo(3);

        JsonFluentAssert.assertThatJson("{\"test\":1}")
                .withMatcher("positive", Matchers.greaterThan(String.valueOf(0)))
                .isPresent();

        JsonFluentAssert.assertThatJson("{\"fields\":[" +
                "{\"key\":1, \"name\":\"AA\"}," +
                "{\"key\":2, \"name\":\"AB\"}," +
                "{\"key\":3, \"name\":\"AC\"}" +
                "]}")
                .whenIgnoringPaths("$.fields[?(@.name=='AA')].key")
                .isEqualTo("{\"fields\":[" +
                        "{\"key\":1, \"name\":\"AA\"}," +
                        "{\"key\":2, \"name\":\"AB\"}," +
                        "{\"key\":3, \"name\":\"AC\"}" +
                        "]}");

        String jsonString = "{\"id\":\"1\", \"children\":[{\"parentId\":\"1\"}]}";

        JsonFluentAssert.assertThatJson(jsonString)
                .when(ConfigurationWhen.path("children"))
                .isPresent();

        JsonFluentAssert.assertThatJson(jsonString)
                .as("verify children has prent id")
                .node("children")
                .isPresent();
    }

    @Test
    void containsEntryShouldWork() {
        String entryValue = "{\n" +
                "  \"approvable\" : true," +
                "  \"rejectable\" : false" +
                "}";

        String input = "[{\"allowedActions\":" + entryValue + "}]";

        JsonAssertions.assertThatJson(input,
                body -> body.isArray().hasSize(1),
                body -> body.inPath("[0]").isObject().containsEntry("allowedActions", json(entryValue)),
                body -> body.inPath("[0]").isObject().contains(entry("allowedActions", json(entryValue))),
                body -> body.inPath("[0]").isObject().containsAllEntriesOf(singletonMap("allowedActions", json(entryValue))),
                body -> body.inPath("[0]").isObject().containsAnyOf(entry("allowedActions", json(entryValue)), entry("test", 1)),
                body -> body.inPath("[0]").isObject().containsExactlyInAnyOrderEntriesOf(singletonMap("allowedActions", json(entryValue))),
                body -> body.inPath("[0]").isObject().containsOnly(entry("allowedActions", json(entryValue))),
                body -> body.inPath("[0]").isObject().containsValues(json(entryValue)),
                body -> body.inPath("[0]").isObject().containsValue(json(entryValue)),
                body -> body.inPath("[0].allowedActions").isObject().isEqualTo(json(entryValue))
        );
    }

    public static Object jsonTreeObject(String value) {
        try {
            return new ObjectMapper().readTree(value);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
            return null;
        }
    }

    public static JsonNode jsonTreeNode(String value) {
        try {
            return new ObjectMapper().readTree(value);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
            return null;
        }
    }
}
