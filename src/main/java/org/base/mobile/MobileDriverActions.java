package org.base.mobile;

import java.util.function.Consumer;

public class MobileDriverActions {
    private final String driverType;
    private final MobileDriverProvider mobileDriverProvider;
    public MobileDriverActions(String driverType, MobileDriverProvider mobileDriverProvider) {
        this.driverType = driverType;
        this.mobileDriverProvider = mobileDriverProvider;
    }

    public DriverType getDriverType() {
        if (this.driverType.equalsIgnoreCase("ANDROID")) {
            return DriverType.ANDROID;
        } else if (this.driverType.equalsIgnoreCase("IOS")) {
            return DriverType.IOS;
        } else return DriverType.UNKNOWN;
    }

    /**
     * action
     * @param setAction
     */
    public void action(Consumer<MobileDriverProvider> setAction) {
        switch (this.getDriverType()) {
            case IOS : case ANDROID: setAction.accept(this.mobileDriverProvider);
            case UNKNOWN : throw new RuntimeException("driver type is not android and not ios");
        }
    }

    /**
     * action
     * @param androidAction
     * @param iosAction
     */
    public void action(Consumer<MobileDriverProvider> androidAction, Consumer<MobileDriverProvider> iosAction) {
        switch (this.getDriverType()) {
            case IOS -> iosAction.accept(this.mobileDriverProvider);
            case ANDROID -> androidAction.accept(this.mobileDriverProvider);
            case UNKNOWN -> throw new RuntimeException("driver type is not android and not ios");
        }
    }
    public enum DriverType {
        ANDROID,
        IOS,
        UNKNOWN
    }
}
