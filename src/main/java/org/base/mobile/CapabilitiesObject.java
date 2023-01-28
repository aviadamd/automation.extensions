package org.base.mobile;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"appPath","appPackage", "automationName", "platformVersion","avd","udid","appBundleId"})
public class CapabilitiesObject {

    private String appPath;
    private String appPackage;
    private String automationName;
    private String platformVersion;
    private String avd;
    private String udid;
    private String appBundleId;

    public CapabilitiesObject() {}
    public CapabilitiesObject(String appPath, String appPackage, String automationName, String platformVersion, String avd, String udid, String appBundleId) {
        this.appPath = appPath;
        this.appPackage = appPackage;
        this.automationName = automationName;
        this.platformVersion = platformVersion;
        this.avd = avd;
        this.udid = udid;
        this.appBundleId = appBundleId;
    }

    public void setAppPath(String appPath) { this.appPath = appPath; }
    public void setAppPackage(String appPackage) { this.appPackage = appPackage; }
    public void setAutomationName(String automationName) { this.automationName = automationName; }
    public void setPlatformVersion(String platformVersion) { this.platformVersion = platformVersion; }
    public void setAvd(String avd) { this.avd = avd; }
    public void setUdid(String udid) { this.udid = udid; }
    public void setAppBundleId(String appBundleId) { this.appBundleId = appBundleId; }
    public String getAppPath() { return appPath; }
    public String getAppPackage() { return appPackage; }
    public String getAutomationName() { return automationName; }
    public String getPlatformVersion() { return platformVersion; }
    public String getAvd() { return avd; }
    public String getUdid() { return udid; }
    public String getAppBundleId() {return appBundleId; }

    @Override
    public String toString() {
        return "CapabilitiesObject{" +
                "appPath='" + appPath + '\'' +
                ", appPackage='" + appPackage + '\'' +
                ", automationName='" + automationName + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", avd='" + avd + '\'' +
                ", udid='" + udid + '\'' +
                ", appBundleId='" + appBundleId + '\'' +
                '}';
    }
}
