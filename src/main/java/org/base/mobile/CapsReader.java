package org.base.mobile;

import org.openqa.selenium.remote.DesiredCapabilities;

public class CapsReader {
    private final DesiredCapabilities capabilities;
    private final CapabilitiesObject jsonObject;
    public CapsReader(DesiredCapabilities capabilities, CapabilitiesObject jsonObject) {
        this.capabilities = capabilities;
        this.jsonObject = jsonObject;
    }

    public CapabilitiesObject getJsonObject() { return this.jsonObject; }
    public DesiredCapabilities getCapabilities() { return this.capabilities; }

    @Override
    public String toString() {
        return "CapsReader{" +
                "capabilities=" + capabilities +
                ", capabilitiesObject=" + jsonObject +
                '}';
    }
}
