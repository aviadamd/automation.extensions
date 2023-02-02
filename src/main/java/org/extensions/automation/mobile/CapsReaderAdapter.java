package org.extensions.automation.mobile;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.files.jsonReader.JacksonReaderExtension;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.time.Duration;
import java.util.Optional;
import static org.automation.mobile.MobileDriverManager.isAndroidClient;
public class CapsReaderAdapter {
    private CapabilitiesObject jsonObject;
    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    public CapabilitiesObject getJsonObject() { return this.jsonObject; }
    public DesiredCapabilities getCapabilities() { return this.capabilities; }

    public CapsReaderAdapter(String jsonPath) {
        try {
            Optional<CapabilitiesObject> capsObject = new JacksonReaderExtension(new File(jsonPath)).readValue(CapabilitiesObject.class);
            if (capsObject.isPresent()) {
                if (isAndroidClient())
                    this.capabilities.merge(this.androidCapabilities(capsObject.get()));
                else this.capabilities.merge(this.iosCapabilities(capsObject.get()));
                this.jsonObject = capsObject.get();
            } else Assertions.fail("fail load capabilities from json " + jsonPath);
        } catch (Exception exception) {
            Assertions.fail("fail load capabilities from json " + jsonPath ,exception);
        }
    }

    private UiAutomator2Options androidCapabilities(CapabilitiesObject jsonObject) {
        return new UiAutomator2Options()
                .setNoReset(true)
                .setAutoGrantPermissions(true)
                .setClearSystemFiles(true)
                .setClearDeviceLogsOnStart(true)
                .setAppWaitForLaunch(true)
                .setAvd(jsonObject.getAvd())
                .setUdid(jsonObject.getUdid())
                .setApp(jsonObject.getAppPath())
                .setAppPackage(jsonObject.getAppBundleId())
                .setPlatformVersion(jsonObject.getPlatformVersion())
                .setDeviceName(jsonObject.getAvd())
                .setAppActivity(jsonObject.getAppBundleId().concat(".features.auth.splash.SplashActivity"))
                .setAndroidInstallTimeout(Duration.ofSeconds(30))
                .setAdbExecTimeout(Duration.ofSeconds(100))
                .setAppWaitDuration(Duration.ofMinutes(1))
                .setNewCommandTimeout(Duration.ofMinutes(1))
                .setUiautomator2ServerInstallTimeout(Duration.ofMinutes(1))
                .setUiautomator2ServerLaunchTimeout(Duration.ofMinutes(1))
                .setUiautomator2ServerReadTimeout(Duration.ofMinutes(1))
                .clearDeviceLogsOnStart()
                .clearSystemFiles();
    }
    private XCUITestOptions iosCapabilities(CapabilitiesObject jsonObject) {
        return new XCUITestOptions()
                .setNoReset(true)
                .setNoReset(true)
                .setClearSystemFiles(true)
                .setUdid(jsonObject.getUdid())
                .setApp(jsonObject.getAppPath())
                .setBundleId(jsonObject.getAppBundleId())
                .setDeviceName(jsonObject.getAvd())
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
