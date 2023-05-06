package org.extensions.automation.mobile;

import org.base.mobile.MobileDriverProvider;
import org.data.files.jsonReader.JacksonObjectAdapter;
import org.utils.mongo.morphia.MorphiaRepository;

public class MobileSharedObjects<T> {
    private MobileDriverProvider driverManager;
    private MorphiaRepository morphiaRepository;
    private JacksonObjectAdapter<T> jacksonObjectAdapter;
    public void setMorphiaRepository(MorphiaRepository morphiaRepository) { this.morphiaRepository = morphiaRepository; }
    public void setDriverManager(MobileDriverProvider driverManager) { this.driverManager = driverManager; }
    public void setJacksonExtension(JacksonObjectAdapter<T> jacksonObjectAdapter) { this.jacksonObjectAdapter = jacksonObjectAdapter; }
    public JacksonObjectAdapter<T> getJacksonExtension() { return this.jacksonObjectAdapter; }
    public MobileDriverProvider getDriverManager() { return driverManager; }
    public MorphiaRepository getMorphiaRepository() { return morphiaRepository; }
}
