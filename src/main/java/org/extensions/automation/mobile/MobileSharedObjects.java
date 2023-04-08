package org.extensions.automation.mobile;

import org.base.mobile.MobileDriverProvider;
import org.data.files.jsonReader.JacksonExtension;
import org.utils.mongo.morphia.MorphiaRepository;

public class MobileSharedObjects<T> {
    private MobileDriverProvider driverManager;
    private MorphiaRepository morphiaRepository;
    private JacksonExtension<T> jacksonExtension;

    public void setMorphiaRepository(MorphiaRepository morphiaRepository) {
        this.morphiaRepository = morphiaRepository;
    }

    public void setDriverManager(MobileDriverProvider driverManager) {
        this.driverManager = driverManager;
    }

    public void setJacksonExtension(JacksonExtension<T> jacksonExtension) {
        this.jacksonExtension = jacksonExtension;
    }

    public JacksonExtension<T> getJacksonExtension() { return this.jacksonExtension; }
    public MobileDriverProvider getDriverManager() {
        return driverManager;
    }
    public MorphiaRepository getMorphiaRepository() {
        return morphiaRepository;
    }
}
