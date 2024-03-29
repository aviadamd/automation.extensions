package org.base.mobile;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"classpath:android.properties", "classpath:mobile.properties"})
public interface MobileConfiguration extends Config {

    @Config.Key("android.caps.json")
    String mobileJsonCapabilitiesLocation();

    @Config.Key("project.entry.location")
    String entryFileLocation();

    void setProperty(String key, String value);
}
