package org.component.jackson;

import lombok.extern.slf4j.Slf4j;
import org.extensions.automation.mobile.CapabilitiesObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.RetryingTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;

@Slf4j
public class JunitPioneerJsonReaderTest {

    @ParameterizedTest
    @RetryingTest()
    @JsonClasspathSource(value = "androidCaps1.json")
    void simpleReader(CapabilitiesObject capabilitiesObject) {
        log.info(capabilitiesObject.getAvd());
        log.info(capabilitiesObject.getClient());
    }
}
