package org.extensions.automation.mobile;

import org.automation.mobile.MobileDriverManager;
import org.files.jsonReader.JacksonExtension;
import org.mongo.morphia.MorphiaRepository;

public class MobileSharedObjects {
    private final MobileDriverManager driverManager;
    private final JacksonExtension<?> jacksonExtension;
    private final MorphiaRepository morphiaRepository;

    public <T> MobileSharedObjects(MobileDriverManager driverManager, JacksonExtension<T> jacksonExtension, MorphiaRepository morphiaRepository) {
        this.driverManager = driverManager;
        this.jacksonExtension = jacksonExtension;
        this.morphiaRepository = morphiaRepository;
    }

    public MobileDriverManager getDriverManager() {
        return driverManager;
    }

    public JacksonExtension<?> getJacksonExtension() {
        return jacksonExtension;
    }

    public MorphiaRepository getMorphiaRepository() {
        return morphiaRepository;
    }
}
