package org.extensions.automation.web;

import com.aventstack.extentreports.Status;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.data.StringsUtilities;
import org.data.date.DateTimeUtilExtension;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.report.ExtentTestManager;
import org.data.files.jsonReader.JacksonExtension;
import org.utils.mongo.morphia.MorphiaRepository;

public class WebSharedObjects<JSON_POJO_CLASS> {
    private JacksonExtension<JSON_POJO_CLASS> jacksonExtension;
    private SeleniumWebDriverProvider driverManager;
    private MorphiaRepository morphiaRepository;
    private WebConfiguration webConfiguration;
    private MobProxyExtension mobProxyExtension;

    protected void setDriverManager(SeleniumWebDriverProvider driverManager) { this.driverManager = driverManager; }
    public void setJacksonExtension(JacksonExtension<JSON_POJO_CLASS> jacksonExtension) {
        this.jacksonExtension = jacksonExtension;
    }
    protected void setMobProxyExtension(MobProxyExtension mobProxyExtension) { this.mobProxyExtension = mobProxyExtension; }
    protected void setWebConfiguration(WebConfiguration webConfiguration) {this.webConfiguration = webConfiguration; }
    protected void setMorphiaRepository(MorphiaRepository morphiaRepository) { this.morphiaRepository = morphiaRepository; }
    public void log(Status status, String desc) { ExtentTestManager.log(status, desc); }
    public SeleniumWebDriverProvider getDriverManager() { return this.driverManager; }
    public JacksonExtension<JSON_POJO_CLASS> getJacksonExtension() {
        return this.jacksonExtension;
    }
    public MorphiaRepository getMorphiaRepository() {
        return this.morphiaRepository;
    }
    public WebConfiguration getWebConfiguration() { return this.webConfiguration; }
    public MobProxyExtension getMobProxyExtension() { return this.mobProxyExtension; }
    public DateTimeUtilExtension getDateTimeUtilExtension() { return new DateTimeUtilExtension(); }
    public StringsUtilities getStringsUtilities() { return new StringsUtilities(); }
}
