package org.extensions.automation.mobile;

import io.appium.java_client.proxy.MethodCallListener;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class AppiumDriverEventListener implements MethodCallListener {

    @Override
    public void beforeCall(Object obj, Method method, Object[] args) {
        MethodCallListener.super.beforeCall(obj, method, args);
    }

    @Override
    public Object call(Object obj, Method method, Object[] args, Callable<?> original) throws Throwable {
        return MethodCallListener.super.call(obj, method, args, original);
    }

    @Override
    public void afterCall(Object obj, Method method, Object[] args, Object result) {
        MethodCallListener.super.afterCall(obj, method, args, result);
    }

    @Override
    public Object onError(Object obj, Method method, Object[] args, Throwable e) throws Throwable {
        return MethodCallListener.super.onError(obj, method, args, e);
    }
}
