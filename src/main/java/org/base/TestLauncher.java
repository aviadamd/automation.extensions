package org.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import java.util.HashMap;

@Slf4j
public class TestLauncher {

    /**
     * run
     * @param directoryPath dir path for classes with tests
     * @param configurationParameters TODO
     * @param testExecutionListeners your listeners class
     */
    public void run(
            String directoryPath, HashMap<String,String> configurationParameters,
            TestExecutionListener... testExecutionListeners) {
        try {

            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                    .request()
                    .selectors(DiscoverySelectors.selectDirectory(directoryPath))
                    .configurationParameters(configurationParameters)
                    .build();

            Launcher launcher = LauncherFactory.create();
            launcher.discover(request);
            launcher.execute(request, testExecutionListeners);

        } catch (Exception exception) {
            Assertions.fail("TestLauncher run exception ", exception);
        }
    }
}
