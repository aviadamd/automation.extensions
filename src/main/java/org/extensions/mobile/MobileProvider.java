package org.extensions.mobile;

import org.base.mobile.AppiumServiceManager;
import org.base.mobile.MobileConfiguration;
import org.base.mobile.MobileDriverProvider;
import org.utils.assertions.AssertJHandler;

public class MobileProvider {
    private MobileDriverProvider driverManager;
    private AppiumServiceManager appiumServiceManager;
    private AssertJHandler assertJHandler;
    private MobileConfiguration mobileConfiguration;

    public void setDriverManager(MobileDriverProvider driverManager) {
        this.driverManager = driverManager;
    }
    public MobileDriverProvider getDriverManager() {
        return driverManager;
    }
    public void setAppiumServiceManager(AppiumServiceManager appiumServiceManager) {this.appiumServiceManager = appiumServiceManager;}
    public AppiumServiceManager getAppiumServiceManager() {
        return appiumServiceManager;
    }
    public void setAssertionsExtension(AssertJHandler assertJHandler) {this.assertJHandler = assertJHandler;}
    public AssertJHandler getAssertionsManager() { return assertJHandler; }
    public void setMobileConfiguration(MobileConfiguration mobileConfiguration) {this.mobileConfiguration = mobileConfiguration;}
    public MobileConfiguration getConfiguration() {
        return mobileConfiguration;
    }

}
