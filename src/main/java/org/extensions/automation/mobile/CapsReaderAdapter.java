package org.extensions.automation.mobile;

import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.data.files.jsonReader.JacksonObjectAdapter;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class CapsReaderAdapter {
    private CapabilitiesObject jsonObject;
    private void setJsonObject(CapabilitiesObject jsonObject) { this.jsonObject = jsonObject; }
    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    public synchronized CapabilitiesObject getJsonObject() { return this.jsonObject; }
    public synchronized DesiredCapabilities getCapabilities() { return this.capabilities; }

    public CapsReaderAdapter(CapabilitiesObject capabilitiesObject) {
        this.setJsonObject(capabilitiesObject);
    }

    public CapsReaderAdapter(String jsonPath) {
        try {

            String path = System.getProperty("user.dir") + "/" + jsonPath;
            JacksonObjectAdapter<CapabilitiesObject> jacksonHelper = new JacksonObjectAdapter<>(path, new File(path), CapabilitiesObject.class);
            CapabilitiesObject capsObject = jacksonHelper.readJson();

            switch (capsObject.getClient()) {
                case "ANDROID" -> this.capabilities.merge(this.androidCapabilities(capsObject));
                case "IOS" -> this.capabilities.merge(this.iosCapabilities(capsObject));
                default -> throw new RuntimeException("no android or ios driver name was provided");
            }

            this.setJsonObject(capsObject);
        } catch (Exception exception) {
            Assertions.fail("fail load capabilities from json " + jsonPath ,exception);
        }
    }

    public synchronized DesiredCapabilities setDesiredCapabilities(String[] keys, String[] values) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        for (int i = 0; i < keys.length; i++) capabilities.setCapability(keys[i], values[i]);
        return capabilities;
    }

    private synchronized UiAutomator2Options androidCapabilities(CapabilitiesObject jsonObject) {
        AndroidInstallApplicationOptions androidInstallApplicationOptions = new AndroidInstallApplicationOptions()
                .withUseSdcardEnabled()
                .withReplaceEnabled()
                .withAllowTestPackagesEnabled()
                .withGrantPermissionsEnabled()
                .withTimeout(Duration.of(100, ChronoUnit.SECONDS));
        DesiredCapabilities androidCaps = new DesiredCapabilities();
        androidInstallApplicationOptions.build().forEach(androidCaps::setCapability);

        return new UiAutomator2Options()
                .setNoReset(true)
                .ignoreHiddenApiPolicyError()
                .merge(androidCaps)
                .setAvdLaunchTimeout(Duration.ofMinutes(1))
                .setAutoGrantPermissions(true)
                .setClearSystemFiles(true)
                .setClearDeviceLogsOnStart(true)
                .setAppWaitForLaunch(true)
                .setUdid(jsonObject.getUdid())
                .setAppPackage(jsonObject.getAppBundleId())
                .setApp(jsonObject.getAppPath())
                .setDeviceName(jsonObject.getAvd())
                .setPlatformVersion(jsonObject.getPlatformVersion())
                .setAppWaitDuration(Duration.ofMinutes(1))
                .setAndroidInstallTimeout(Duration.ofSeconds(30))
                .setAdbExecTimeout(Duration.ofSeconds(100))
                .setUiautomator2ServerInstallTimeout(Duration.ofMinutes(1))
                .setUiautomator2ServerLaunchTimeout(Duration.ofMinutes(1))
                .setUiautomator2ServerReadTimeout(Duration.ofMinutes(1))
                .setNewCommandTimeout(Duration.ofMinutes(1));
    }

    private synchronized XCUITestOptions iosCapabilities(CapabilitiesObject jsonObject) {
        return new XCUITestOptions()
                .setNoReset(true)
                .setNoReset(true)
                .setClearSystemFiles(true)
                .setApp(jsonObject.getAppPath())
                .setBundleId(jsonObject.getAppBundleId())
                .setCommandTimeouts(Duration.ofMinutes(1))
                .setPlatformVersion(jsonObject.getPlatformVersion())
                .clearSystemFiles();
    }

    @Override
    public String toString() {
        return "CapsReader{" +
                "capabilities=" + capabilities +
                ", capabilitiesObject=" + jsonObject +
                '}';
    }
}
