package org.extensions.web;

import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.extensions.proxy.MobProxyExtension;
import org.utils.assertions.AssertJHandler;

public class WebSharedObjects {
    private SeleniumWebDriverProvider driverManager;
    private WebConfiguration webConfiguration;
    private MobProxyExtension mobProxyExtension;
    private AssertJHandler assertJHandler;

    protected void setAssertionsManager(AssertJHandler assertJHandler) { this.assertJHandler = assertJHandler; }
    protected void setDriverManager(SeleniumWebDriverProvider driverManager) { this.driverManager = driverManager; }
    protected void setMobProxyExtension(MobProxyExtension mobProxyExtension) { this.mobProxyExtension = mobProxyExtension; }
    protected void setWebConfiguration(WebConfiguration webConfiguration) {this.webConfiguration = webConfiguration; }
    public SeleniumWebDriverProvider getDriverManager() { return this.driverManager; }
    public WebConfiguration getWebConfiguration() { return this.webConfiguration; }
    public MobProxyExtension getMobProxyExtension() { return this.mobProxyExtension; }
    public AssertJHandler getAssertionsManager() { return this.assertJHandler; }

}
