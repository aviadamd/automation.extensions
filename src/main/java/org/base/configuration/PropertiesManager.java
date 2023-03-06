package org.base.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;
import org.aeonbits.owner.ConfigFactory;
import java.util.Map;
public class PropertiesManager {

    /**
     * create new property instance
     * @param tClass class configuration the extends Config interface
     * @return your class instance
     */
    public <T extends Config> T create(Class<T> tClass) {
        return ConfigFactory.create(tClass);
    }
        /**
         * get or create new property instance
         * @param tClass class configuration the extends Config interface
         * @return your class instance
         */
    public <T extends Config> T getOrCreate(Class<T> tClass) {
        return ConfigCache.getOrCreate(tClass);
    }

    /**
     * create new property instance
     * @param tClass class configuration the extends Config interface
     * @param imports new properties to load
     * @return your class instance
     */
    public <T extends Config> T create(Class<T> tClass, Map<?, ?>... imports) {
        return ConfigFactory.create(tClass, imports);
    }

    /**
     * get or create new property instance
     * @param tClass class configuration the extends Config interface
     * @param imports new properties to load
     * @return your class instance
     */
    public <T extends Config> T getOrCreate(Class<T> tClass, Map<?, ?>... imports) {
        return ConfigCache.getOrCreate(tClass, imports);
    }
}
