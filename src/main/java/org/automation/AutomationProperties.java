package org.automation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class AutomationProperties {
    private final Properties configProp = new Properties();
    public static AutomationProperties getPropertiesInstance() {
        return LazyHolder.INSTANCE;
    }

    private AutomationProperties() {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("platform.properties");
            log.debug("load application.application file");
            configProp.load(inputStream);
        } catch (Exception exception) {
            Assertions.fail("load application.application file", exception);
        }
    }
    private static class LazyHolder {
        private static final AutomationProperties INSTANCE = new AutomationProperties();
    }

    public String getProperty(String key) {
        return configProp.getProperty(key);
    }

    public Set<String> getAllPropertyNames() {
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return configProp.containsKey(key);
    }
}
