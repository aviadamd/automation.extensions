package org.base.mobile;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.goebl.david.WebbException;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.File;
import java.time.Duration;
import java.util.Map;

@Slf4j
public class AppiumServiceManager {
    private AppiumDriverLocalService localService;
    private final int port;
    public String ip;

    public AppiumDriverLocalService getLocalService() {
        return localService;
    }

    /**
     *
     * @param nodeJs
     * @param appiumExecutable
     * @param ip
     * @param port
     * @param androidHome
     */
    public AppiumServiceManager(
            String nodeJs, String appiumExecutable,
            String ip, int port, String androidHome) {

        this.ip = ip;
        this.port = port;

        if (this.isServerRunning(ip, String.valueOf(port))) {
            return;
        }

        localService = new AppiumServiceBuilder()
                .usingDriverExecutable(new File(nodeJs))
                .withAppiumJS(new File(appiumExecutable))
                .withIPAddress(ip)
                .usingPort(port)
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withTimeout(Duration.ofMinutes(2))
                .withEnvironment(Map.of("ANDROID_HOME", androidHome))
                .build();
        localService.start();
    }

    public void close() {
        if (this.isServerRunning(this.ip, String.valueOf(this.port))) {
            this.localService.close();
        }
    }

    public boolean isServerRunning(String ip, String port) {
        try {
            Response<JSONObject> appiumResponse = Webb.create()
                    .get("http://"+ip+":"+port+"/status")
                    .asJsonObject();
            return appiumResponse.isSuccess() && !appiumResponse.getResponseMessage().isEmpty();
        } catch (Exception exception) {
            return false;
        }
    }
}
