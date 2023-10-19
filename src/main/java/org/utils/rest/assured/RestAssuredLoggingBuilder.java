package org.utils.rest.assured;

import io.restassured.config.*;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;
import java.io.PrintStream;
import java.util.List;


@Data
@Slf4j
@Builder
public class RestAssuredLoggingBuilder {

    private String baseUri;

    @Builder.Default
    public List<RestAssuredConfig> configs = List.of(
            RestAssuredConfig.config().csrfConfig(CsrfConfig.csrfConfig().loggingEnabled()),
            RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().allowAllHostnames()),
            RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()),
            RestAssuredConfig.config().logConfig(LogConfig.logConfig().defaultStream(new PrintStream(new LogOutputStream() {
                @Override
                protected void processLine(String message, int i) {
                    log.debug(message);
                }
            })))
    );

    @Builder.Default
    public RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter(LogDetail.ALL, new PrintStream(new LogOutputStream() {
        @Override
        protected void processLine(String message, int i) {
            log.debug(message);
        }
    }));

    @Builder.Default
    public ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter(LogDetail.ALL, new PrintStream(new LogOutputStream() {
        @Override
        protected void processLine(String message, int i) {
            log.debug(message);
        }
    }));


    @Builder.Default
    public ErrorLoggingFilter errorLoggingFilter = new ErrorLoggingFilter(new PrintStream(new LogOutputStream() {
        @Override
        protected void processLine(String message, int i) {
            log.error(message);
        }
    }));
}
