package org.extensions.automation.web;

import com.aventstack.extentreports.Status;
import org.automation.web.SeleniumWebDriverManager;
import org.automation.web.WebConfiguration;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.report.ExtentTestManager;
import org.files.jsonReader.JacksonExtension;
import org.mongo.morphia.MorphiaRepository;

public class WebSharedObjects {
    private SeleniumWebDriverManager driverManager;
    private JacksonExtension<?> jacksonExtension;
    private MorphiaRepository morphiaRepository;
    private WebConfiguration webConfiguration;
    private MobProxyExtension mobProxyExtension;
    protected void setDriverManager(SeleniumWebDriverManager driverManager) {
        this.driverManager = driverManager;
    }
    protected void setJacksonExtension(JacksonExtension<?> jacksonExtension) { this.jacksonExtension = jacksonExtension; }
    protected void setMobProxyExtension(MobProxyExtension mobProxyExtension) { this.mobProxyExtension = mobProxyExtension; }
    protected void setWebConfiguration(WebConfiguration webConfiguration) {this.webConfiguration = webConfiguration; }
    protected void setMorphiaRepository(MorphiaRepository morphiaRepository) { this.morphiaRepository = morphiaRepository; }
    public void log(Status status, String desc) { ExtentTestManager.log(status, desc); }

    public SeleniumWebDriverManager getDriverManager() { return this.driverManager; }
    public JacksonExtension<?> getJacksonExtension() {
        return this.jacksonExtension;
    }
    public MorphiaRepository getMorphiaRepository() {
        return this.morphiaRepository;
    }
    public WebConfiguration getWebConfiguration() { return this.webConfiguration; }
    public MobProxyExtension getMobProxyExtension() { return this.mobProxyExtension; }
}
