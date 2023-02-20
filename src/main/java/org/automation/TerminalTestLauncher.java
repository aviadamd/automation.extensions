package org.automation;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import static java.lang.System.getProperty;

@Slf4j
public class TerminalTestLauncher {

    public static void main(String[] args) {

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(DiscoverySelectors.selectClass(getProperty("project.test.class")))
                .configurationParameter("junit.conditions.deactivate", getProperty("project.test.extensions"))
                .build();

        TestPlan plan = LauncherFactory.create().discover(request);
        if (plan.containsTests()) {
            Launcher launcher = LauncherFactory.create();
            SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
            launcher.execute(request, summaryGeneratingListener);
            log.debug(" " + summaryGeneratingListener.getSummary());
        }
    }
}
