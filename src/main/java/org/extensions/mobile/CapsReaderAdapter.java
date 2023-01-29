package org.extensions.mobile;

import org.files.jsonReader.JsonReaderExtensions;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.util.Optional;

public class CapsReaderAdapter {
    public CapsReader read(String jsonPath)  {
        try {

            JsonReaderExtensions readerExtensions = new JsonReaderExtensions(new File(System.getProperty(jsonPath)));
            Optional<CapabilitiesObject> capsObject = readerExtensions.readValue(CapabilitiesObject.class);

            if (capsObject.isPresent()) {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("appium:app", capsObject.get().getAppPath());
                capabilities.setCapability("appium:appPackage", capsObject.get().getAppPackage());
                capabilities.setCapability("appium:automationName", capsObject.get().getAutomationName());
                capabilities.setCapability("appium:platformVersion", capsObject.get().getPlatformVersion());
                capabilities.setCapability("appium:avd", capsObject.get().getAvd());
                capabilities.setCapability("appium:udid", capsObject.get().getUdid());
                capabilities.setCapability("appium:newCommandTimeout", 15000);
                capabilities.setCapability("appium:autoGrantPermissions",true);
                capabilities.setCapability("appium:noReset",true);
                return new CapsReader(capabilities, capsObject.get());
            }

            Assertions.fail("fail load capabilities from json " + jsonPath);
        } catch (Exception exception) {
            Assertions.fail("fail load capabilities from json " + jsonPath ,exception);
        }

        return null;
    }
}
