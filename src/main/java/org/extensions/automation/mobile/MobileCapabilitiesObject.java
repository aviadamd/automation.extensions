package org.extensions.automation.mobile;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"appiumExe","nodeJs","appiumPort","appiumIp","client","driverUrl","appPath", "platformVersion","avd","udid","appBundleId","androidHome"})
public class MobileCapabilitiesObject {

    private String androidHome;
    private String appiumExe;
    private String nodeJs;
    private String appiumPort;
    private String appiumIp;
    private String client;
    private String driverUrl;
    private String appPath;
    private String platformVersion;
    private String avd;
    private String udid;
    private String appBundleId;

    public MobileCapabilitiesObject() {}

    /**
     *
     * @param appiumExe
     * @param nodeJs
     * @param appiumPort
     * @param appiumIp
     * @param client
     * @param driverUrl
     * @param appPath
     * @param platformVersion
     * @param avd
     * @param udid
     * @param appBundleId
     */
    public MobileCapabilitiesObject(
            String appiumExe, String nodeJs, String appiumPort,
            String appiumIp, String client, String driverUrl,
            String appPath, String platformVersion, String avd,
            String udid, String appBundleId, String androidHome) {
        this.appiumExe = appiumExe;
        this.nodeJs = nodeJs;
        this.appiumPort = appiumPort;
        this.appiumIp = appiumIp;
        this.client = client;
        this.driverUrl = driverUrl;
        this.appPath = appPath;
        this.platformVersion = platformVersion;
        this.avd = avd;
        this.udid = udid;
        this.appBundleId = appBundleId;
        this.androidHome = androidHome;
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

    public String getAppiumExe() {
        return appiumExe;
    }

    public void setAppiumExe(String appiumExe) {
        this.appiumExe = appiumExe;
    }

    public String getAppiumIp() {
        return appiumIp;
    }

    public void setAppiumIp(String appiumIp) {
        this.appiumIp = appiumIp;
    }

    public String getNodeJs() {
        return nodeJs;
    }

    public void setNodeJs(String nodeJs) {
        this.nodeJs = nodeJs;
    }

    public String getAppiumPort() {
        return appiumPort;
    }

    public void setAppiumPort(String appiumPort) {
        this.appiumPort = appiumPort;
    }

    public void setAndroidHome(String androidHome) {
        this.androidHome = androidHome;
    }

    public String getAndroidHome() {
        return androidHome;
    }


}
