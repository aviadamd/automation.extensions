package org.base;

import io.appium.java_client.AppiumFluentWait;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaiterHandler {
    private ThreadLocal<AppiumFluentWait<Class<?>>> fluentWait = new ThreadLocal<>();
    public AppiumFluentWait<Class<?>> getFluentWait() { return this.fluentWait.get(); }

    public WaiterHandler(Class<?> type) {
        this.fluentWait.set(new AppiumFluentWait<>(type));
    }

}
