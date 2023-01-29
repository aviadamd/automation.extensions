package org.base.mobile;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"client", "appiumUrl","appPath","appPackage", "automationName", "platformVersion","avd","udid","appBundleId"})
public class CapabilitiesObject {
    private String client;
    private String appiumUrl;
    private String appPath;
    private String appPackage;
    private String automationName;
    private String platformVersion;
    private String avd;
    private String udid;
    private String appBundleId;

    public CapabilitiesObject() {}
    public CapabilitiesObject(String client, String appiumUrl, String appPath, String appPackage, String automationName, String platformVersion, String avd, String udid, String appBundleId) {
        this.client = client;
        this.appiumUrl = appiumUrl;
        this.appPath = appPath;
        this.appPackage = appPackage;
        this.automationName = automationName;
        this.platformVersion = platformVersion;
        this.avd = avd;
        this.udid = udid;
        this.appBundleId = appBundleId;
    }

    public void setClient(String client) { this.client = client; }
    public void setAppiumUrl(String appiumUrl) { this.appiumUrl = appiumUrl; }
    public void setAppPath(String appPath) { this.appPath = appPath; }
    public void setAppPackage(String appPackage) { this.appPackage = appPackage; }
    public void setAutomationName(String automationName) { this.automationName = automationName; }
    public void setPlatformVersion(String platformVersion) { this.platformVersion = platformVersion; }
    public void setAvd(String avd) { this.avd = avd; }
    public void setUdid(String udid) { this.udid = udid; }
    public void setAppBundleId(String appBundleId) { this.appBundleId = appBundleId; }

    public String getClient() { return this.client; }
    public String getAppiumUrl() { return this.appiumUrl; }
    public String getAppPath() { return this.appPath; }
    public String getAppPackage() { return this.appPackage; }
    public String getAutomationName() { return this.automationName; }
    public String getPlatformVersion() { return this.platformVersion; }
    public String getAvd() { return this.avd; }
    public String getUdid() { return this.udid; }
    public String getAppBundleId() { return this.appBundleId; }

    @Override
    public String toString() {
        return "CapabilitiesObject{" +
                "client='" + client + '\'' +
                ", appiumUrl='" + appiumUrl + '\'' +
                ", appPath='" + appPath + '\'' +
                ", appPackage='" + appPackage + '\'' +
                ", automationName='" + automationName + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", avd='" + avd + '\'' +
                ", udid='" + udid + '\'' +
                ", appBundleId='" + appBundleId + '\'' +
                '}';
    }
}
