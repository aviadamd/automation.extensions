package org.extensions.automation.mobile;

import org.base.mobile.MobileDriverProvider;
import org.data.files.jsonReader.JacksonObjectAdapter;
import org.utils.mongo.morphia.MorphiaRepository;

public class MobileSharedObjects {
    private MobileDriverProvider driverManager;
    public void setDriverManager(MobileDriverProvider driverManager) { this.driverManager = driverManager; }
    public MobileDriverProvider getDriverManager() { return driverManager; }
}
