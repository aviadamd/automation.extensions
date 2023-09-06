package org.base.mobile;

import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AppiumServiceManager {
    private final String ip;
    private final int port;
    private final String nodeJs;
    private final String appiumExecutable;

    public AppiumServiceManager(String nodeJs, String appiumExecutable, String ip, int port) {
        this.nodeJs = nodeJs;
        this.appiumExecutable = appiumExecutable;
        this.ip = ip;
        this.port = port;
    }

    public void initService() {
        new AppiumServiceBuilder()
                .usingDriverExecutable(new File(this.nodeJs))
                .withAppiumJS(new File(this.appiumExecutable))
                .withIPAddress(this.ip)
                .usingPort(this.port)
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withTimeout(Duration.ofMinutes(2))
                .withEnvironment(Map.of("ANDROID_HOME", "C:\\Users\\Lenovo\\OneDrive\\Desktop\\Android\\Sdk"))
                .build()
                .start();
    }
}
