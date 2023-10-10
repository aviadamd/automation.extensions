package org.extensions.web;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v108.browser.Browser;
import org.openqa.selenium.devtools.v108.log.Log;
import org.openqa.selenium.devtools.v108.network.Network;
import org.openqa.selenium.devtools.v108.runtime.Runtime;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.Optional;

@Slf4j
public class DevToolsListener {

    private synchronized void addDriverDevTools(WebDriver driver) {
        DevTools devTools;

        if (driver instanceof ChromeDriver) {
            devTools = ((ChromeDriver) driver).getDevTools();
        } else if (driver instanceof FirefoxDriver) {
            devTools = ((FirefoxDriver) driver).getDevTools();
        } else throw new IllegalStateException("Unexpected value: " + ((HasCapabilities) driver).getCapabilities().getBrowserName());

        devTools.send(Network.enable(Optional.of(100000000), Optional.empty(), Optional.empty()));
        devTools.createSession(driver.getWindowHandle());
        devTools.send(Log.enable());
        devTools.send(Runtime.enable());
        devTools.send(Network.enableReportingApi(true));
        devTools.send(Browser.getVersion());
        devTools.send(Browser.getBrowserCommandLine());
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.responseReceived(), requestSent -> {
            log.debug("URL => " + requestSent.getResponse().getUrl());
            log.debug("Status => " + requestSent.getResponse().getStatus());
            log.debug("Headers => " + requestSent.getResponse().getHeaders().toString());
            log.debug("------------------------------------------------------");
        });

    }
}
