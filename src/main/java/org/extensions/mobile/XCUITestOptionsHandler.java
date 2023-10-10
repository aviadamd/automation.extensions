package org.extensions.mobile;

import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import java.time.Duration;

public class XCUITestOptionsHandler {

    public XCUITestOptions xcuiTestOptions(MobileCapabilitiesObject jsonObject) {
        return new XCUITestOptions()
                .setNoReset(true)
                .setApp(jsonObject.getAppPath())
                .setBundleId(jsonObject.getAppBundleId())
                .setCommandTimeouts(Duration.ofMinutes(1))
                .setPlatformName(MobilePlatform.IOS)
                .setAutomationName(AutomationName.IOS_XCUI_TEST)
                .setPlatformVersion(jsonObject.getPlatformVersion());
    }
}
