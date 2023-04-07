package org.extensions.automation.mobile;

import org.base.mobile.MobileDriverProvider;
import org.utils.mongo.morphia.MorphiaRepository;

public class MobileSharedObjects {
    private final MobileDriverProvider driverManager;
    private final MorphiaRepository morphiaRepository;

    /**
     * MobileSharedObjects
     * @param driverManager
     * @param morphiaRepository
     */
    public MobileSharedObjects(MobileDriverProvider driverManager, MorphiaRepository morphiaRepository) {
        this.driverManager = driverManager;
        this.morphiaRepository = morphiaRepository;
    }

    public MobileDriverProvider getDriverManager() {
        return driverManager;
    }
    public MorphiaRepository getMorphiaRepository() {
        return morphiaRepository;
    }
}
