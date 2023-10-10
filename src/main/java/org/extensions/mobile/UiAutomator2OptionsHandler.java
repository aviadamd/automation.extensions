package org.extensions.mobile;

import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.extensions.mobile.MobileCapabilitiesObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class UiAutomator2OptionsHandler {

    public UiAutomator2Options uiAutomator2Options(MobileCapabilitiesObject jsonObject) {
        AndroidInstallApplicationOptions androidInstallApplicationOptions = new AndroidInstallApplicationOptions()
                .withUseSdcardEnabled()
                .withAllowTestPackagesEnabled()
                .withTimeout(Duration.of(100, ChronoUnit.SECONDS));
        DesiredCapabilities androidCaps = new DesiredCapabilities();
        androidInstallApplicationOptions.build().forEach(androidCaps::setCapability);

        return new UiAutomator2Options()
                .setNoReset(true)
                .merge(androidCaps)
                .setIgnoreHiddenApiPolicyError(true)
                .setAvdLaunchTimeout(Duration.ofMinutes(1))
                .setAutoGrantPermissions(true)
                .setClearSystemFiles(true)
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

}
