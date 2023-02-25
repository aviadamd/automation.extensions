package org.extensions.automation.web;

import com.aventstack.extentreports.Status;
import org.automation.web.SeleniumWebDriverProvider;
import org.automation.web.WebConfiguration;
import org.data.StringsUtilities;
import org.date.DateTimeUtilExtension;
import org.extensions.automation.proxy.MobProxyExtension;
import org.extensions.report.ExtentTestManager;
import org.files.jsonReader.JacksonExtension;
import org.mongo.morphia.MorphiaRepository;

public class WebSharedObjects {
    private SeleniumWebDriverProvider driverManager;
    private JacksonExtension<?> jacksonExtension;
    private MorphiaRepository morphiaRepository;
    private WebConfiguration webConfiguration;
    private MobProxyExtension mobProxyExtension;
    private DateTimeUtilExtension dateTimeUtilExtension;
    private StringsUtilities stringsUtilities;

    protected void setDriverManager(SeleniumWebDriverProvider driverManager) {
        this.driverManager = driverManager;
    }
    protected void setJacksonExtension(JacksonExtension<?> jacksonExtension) { this.jacksonExtension = jacksonExtension; }
    protected void setMobProxyExtension(MobProxyExtension mobProxyExtension) { this.mobProxyExtension = mobProxyExtension; }
    protected void setWebConfiguration(WebConfiguration webConfiguration) {this.webConfiguration = webConfiguration; }
    protected void setMorphiaRepository(MorphiaRepository morphiaRepository) { this.morphiaRepository = morphiaRepository; }
    public void log(Status status, String desc) { ExtentTestManager.log(status, desc); }

    public SeleniumWebDriverProvider getDriverManager() { return this.driverManager; }
    public JacksonExtension<?> getJacksonExtension() {
        return this.jacksonExtension;
    }
    public MorphiaRepository getMorphiaRepository() {
        return this.morphiaRepository;
    }
    public WebConfiguration getWebConfiguration() { return this.webConfiguration; }
    public MobProxyExtension getMobProxyExtension() { return this.mobProxyExtension; }

    public DateTimeUtilExtension getDateTimeUtilExtension() {
        this.dateTimeUtilExtension = new DateTimeUtilExtension();
        return this.dateTimeUtilExtension;
    }

    public StringsUtilities getStringsUtilities() {
        this.stringsUtilities = new StringsUtilities();
        return this.stringsUtilities;
    }
}
