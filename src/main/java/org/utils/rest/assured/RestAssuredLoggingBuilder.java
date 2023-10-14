package org.utils.rest.assured;

import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;
import org.slf4j.event.Level;
import java.io.PrintStream;
import java.util.List;
import static io.restassured.RestAssured.config;
import static io.restassured.config.FailureConfig.failureConfig;
import static io.restassured.config.LogConfig.logConfig;

@Data
@Slf4j
@Builder
public class RestAssuredLoggingBuilder {

    private String baseUri;

    @Builder.Default
    private List<RestAssuredConfig> configs = List.of(
            config().failureConfig(failureConfig().with().failureListeners((requestSpec, responseSpec, response) -> log.error("response failure: " + response.asPrettyString()))),
            config().logConfig(logConfig().enablePrettyPrinting(true).defaultStream(new PrintStream(printStream(Level.DEBUG,""))))
    );

    @Builder.Default
    private List<Filter> filters = List.of(
            new RequestLoggingFilter(LogDetail.ALL, new PrintStream(printStream(Level.INFO,"request: "))),
            new ResponseLoggingFilter(LogDetail.ALL, new PrintStream(printStream(Level.INFO,"response: "))),
            new ErrorLoggingFilter(new PrintStream(printStream(Level.ERROR,"error: ")))
    );


    private static PrintStream printStream(Level level, String category) {
        return new PrintStream(new LogOutputStream() {
            @Override
            protected void processLine(String message, int i) {
                log.atLevel(level).log(category + " " + message);
            }
        });
    }
}
