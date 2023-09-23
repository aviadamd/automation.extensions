package org.base.mobile;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import io.appium.java_client.service.local.*;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import java.io.File;
import java.time.Duration;
import java.util.Map;

@Slf4j
public class AppiumServiceManager {
    private AppiumDriverLocalService localService;
    private final int port;
    private final String ip;

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

        try {

            if (this.isServerRunning(ip, String.valueOf(port))) return;

            localService = new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(nodeJs))
                    .withAppiumJS(new File(appiumExecutable))
                    .withIPAddress(ip)
                    .usingPort(port)
                    .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                    .withArgument(GeneralServerFlag.ALLOW_INSECURE, "ALLOW_INSECURE")
                    .withTimeout(Duration.ofMinutes(2))
                    .withEnvironment(Map.of("ANDROID_HOME", androidHome))
                    .build();
            localService.start();

        } catch (Exception exception) {
            this.onFail(exception);
        }
    }

    public void close() {
        try {
            if (this.isServerRunning(this.ip, String.valueOf(this.port))) {
                this.localService.close();
            }
        } catch (Exception ignore) {}

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

    private void onFail(Exception exception) {
        if (exception instanceof AppiumServerHasNotBeenStartedLocallyException) {
            Assertions.fail("AppiumServerHasNotBeenStartedLocallyException " + exception.getMessage());
        } else if (exception instanceof InvalidNodeJSInstance) {
            Assertions.fail("InvalidNodeJSInstance " + exception.getMessage());
        } else if (exception instanceof InvalidServerInstanceException) {
            Assertions.fail("InvalidServerInstanceException " + exception.getMessage());
        } else Assertions.fail("Exception " + exception.getMessage());
    }


//        WaiterHandler waiterHandler = new WaiterHandler(Boolean.class);
//        boolean isAppiumUp = waiterHandler
//                .getFluentWait()
//                .withTimeout(Duration.ofSeconds(5))
//                .pollingEvery(Duration.ofSeconds(1))
//                .until(e -> localService.isRunning());
//        Assertions.assertThat(isAppiumUp).isTrue();

//        Awaitility.pollExecutorService(Executors.newFixedThreadPool(3));
//        Awaitility.await().atMost(Duration.ofSeconds(3)).until(localService::isRunning);
}
