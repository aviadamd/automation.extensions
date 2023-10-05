package org.extensions.automation.mobile;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.utils.files.jsonReader.JacksonObjectAdapter;
import org.extensions.automation.UiAutomator2OptionsHandler;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;

public class CapsReaderAdapter {
    private MobileCapabilitiesObject jsonObject;
    private void setJsonObject(MobileCapabilitiesObject jsonObject) { this.jsonObject = jsonObject; }
    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    public synchronized MobileCapabilitiesObject getJsonObject() { return this.jsonObject; }
    public synchronized DesiredCapabilities getCapabilities() { return this.capabilities; }

    public CapsReaderAdapter(MobileCapabilitiesObject mobileCapabilitiesObject,
                             UiAutomator2Options uiAutomator2OptionsExtra,
                             XCUITestOptions xcuiTestOptionsExtra) {

        Assertions.assertNotNull(mobileCapabilitiesObject);

        switch (mobileCapabilitiesObject.getClient()) {
            case "ANDROID" -> {
                UiAutomator2OptionsHandler uiAutomator2OptionsHandler = new UiAutomator2OptionsHandler();
                this.capabilities.merge(uiAutomator2OptionsHandler.uiAutomator2Options(mobileCapabilitiesObject));
                if (uiAutomator2OptionsExtra != null) {
                    this.capabilities.merge(uiAutomator2OptionsExtra);
                }
            }
            case "IOS" -> {
                XCUITestOptionsHandler xcuiTestOptionsHandler = new XCUITestOptionsHandler();
                this.capabilities.merge(xcuiTestOptionsHandler.xcuiTestOptions(mobileCapabilitiesObject));
                if (xcuiTestOptionsExtra != null) {
                    this.capabilities.merge(xcuiTestOptionsExtra);
                }
            }
            default -> throw new RuntimeException("no android or ios driver name was provided");
        }

        this.setJsonObject(mobileCapabilitiesObject);
    }

    public CapsReaderAdapter(String jsonPath) {
        try {

            Assertions.assertTrue(jsonPath != null && !jsonPath.isEmpty());

            String path = ClassLoader.getSystemResource(jsonPath).getPath();
            JacksonObjectAdapter<MobileCapabilitiesObject> jacksonHelper = new JacksonObjectAdapter<>(path, new File(path), MobileCapabilitiesObject.class);
            MobileCapabilitiesObject capsObject = jacksonHelper.readJson();

            switch (capsObject.getClient()) {
                case "ANDROID" -> {
                    UiAutomator2OptionsHandler uiAutomator2OptionsHandler = new UiAutomator2OptionsHandler();
                    UiAutomator2Options uiAutomator2Options = uiAutomator2OptionsHandler.uiAutomator2Options(capsObject);
                    this.capabilities.merge(uiAutomator2Options);
                }
                case "IOS" -> {
                    XCUITestOptionsHandler xcuiTestOptionsHandler = new XCUITestOptionsHandler();
                    XCUITestOptions xcuiTestOptions = xcuiTestOptionsHandler.xcuiTestOptions(capsObject);
                    this.capabilities.merge(xcuiTestOptions);
                }
                default -> throw new RuntimeException("no android or ios driver name was provided");
            }

            this.setJsonObject(capsObject);
        } catch (Exception exception) {
            Assertions.fail("fail load capabilities from json " + jsonPath ,exception);
        }
    }

    @Override
    public String toString() {
        return "CapsReaderAdapter{" +
                "jsonObject=" + jsonObject +
                ", capabilities=" + capabilities +
                '}';
    }
}
