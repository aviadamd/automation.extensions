package org.base.mobile;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppiumServiceManager {
    private final String ip;
    private final int port;
    private String appiumBasePath = "/wd/hub";
    private final String nodeJs;
    private final String appiumExecutable;
    private List<ServerArgument> serverArgumentList = new ArrayList<>();
    private HashMap<ServerArgument, String> serverArgumentsMap = new HashMap<>();


    public AppiumServiceManager addAppiumBasePath(String appiumBasePath) {
        this.appiumBasePath = appiumBasePath;
        return this;
    }

    public AppiumServiceManager addServerArgsList(List<ServerArgument> serverArgumentList) {
        this.serverArgumentList = serverArgumentList;
        return this;
    }

    public AppiumServiceManager addServerArgsMap(HashMap<ServerArgument, String> serverArgumentsMap) {
        this.serverArgumentsMap = serverArgumentsMap;
        return this;
    }

    public AppiumServiceManager(String nodeJs, String appiumExecutable, String ip, int port) {
        this.nodeJs = nodeJs;
        this.appiumExecutable = appiumExecutable;
        this.ip = ip;
        this.port = port;
    }

    public AppiumDriverLocalService initService() {
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();

        if (!this.serverArgumentsMap.isEmpty()) this.serverArgumentsMap.forEach(serviceBuilder::withArgument);
        if (!this.serverArgumentList.isEmpty()) this.serverArgumentList.forEach(serviceBuilder::withArgument);

        return serviceBuilder
                .usingDriverExecutable(new File(this.nodeJs))
                .withAppiumJS(new File(this.appiumExecutable))
                .withIPAddress(this.ip).usingPort(this.port)
                .withArgument(GeneralServerFlag.BASEPATH, this.appiumBasePath)
                .withArgument(GeneralServerFlag.ALLOW_INSECURE)
                .withArgument(GeneralServerFlag.ASYNC_TRACE)
                .build();
    }
}
