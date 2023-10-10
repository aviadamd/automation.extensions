package org.component.jackson;

import lombok.extern.slf4j.Slf4j;
import org.extensions.mobile.MobileCapabilitiesObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.RetryingTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;

@Slf4j
public class JunitPioneerJsonReaderTest {

    @ParameterizedTest
    @RetryingTest()
    @JsonClasspathSource(value = "devicesCapabilities1.json")
    void simpleReader(MobileCapabilitiesObject mobileCapabilitiesObject) {
        log.info(mobileCapabilitiesObject.getAvd());
        log.info(mobileCapabilitiesObject.getClient());
    }
}
