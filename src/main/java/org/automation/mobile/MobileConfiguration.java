package org.automation.mobile;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"classpath:android.properties", "classpath:mobile.properties"})
public interface MobileConfiguration extends Config {

    @Config.Key("project.appium.exe")
    String appiumExecutable();

    @Config.Key("project.node.js")
    String nodeJsExecutable();

    @Config.Key("project.mobile.client")
    String mobileClient();
    @Config.Key("android.caps.json")
    String mobileJsonCapabilities();

    @Config.Key("project.entry.location")
    String entryFileLocation();
    void setProperty(String key, String value);
}
