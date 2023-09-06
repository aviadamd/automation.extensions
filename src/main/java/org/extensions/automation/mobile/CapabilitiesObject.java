package org.extensions.automation.mobile;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({"client","driverUrl","appPath", "platformVersion","avd","udid","appBundleId"})
public class CapabilitiesObject {
    private String client;
    private String driverUrl;
    private String appPath;
    private String platformVersion;
    private String avd;
    private String udid;
    private String appBundleId;
    public CapabilitiesObject() {}
    public CapabilitiesObject(String client, String driverUrl, String appPath, String platformVersion, String avd, String udid, String appBundleId) {
        this.client = client;
        this.driverUrl = driverUrl;
        this.appPath = appPath;
        this.platformVersion = platformVersion;
        this.avd = avd;
        this.udid = udid;
        this.appBundleId = appBundleId;
    }

    public String getClient() {
        return this.client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDriverUrl() {
        return driverUrl;
    }

    public void setDriverUrl(String driverUrl) {
        this.driverUrl = driverUrl;
    }

    public String getAppPath() {
        return this.appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getPlatformVersion() {
        return this.platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getAvd() {
        return this.avd;
    }

    public void setAvd(String avd) {
        this.avd = avd;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getAppBundleId() {
        return this.appBundleId;
    }

    public void setAppBundleId(String appBundleId) {
        this.appBundleId = appBundleId;
    }

    @Override
    public String toString() {
        return "CapabilitiesObject{" +
                "client='" + client + '\'' +
                ", driverUrl='" + driverUrl + '\'' +
                ", appPath='" + appPath + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", avd='" + avd + '\'' +
                ", udid='" + udid + '\'' +
                ", appBundleId='" + appBundleId + '\'' +
                '}';
    }
}
