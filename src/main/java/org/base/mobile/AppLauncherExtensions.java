package org.base.mobile;

import org.extensions.mobile.ApplicationLaunchOption;

public class AppLauncherExtensions {
    private final MobileDriverProvider mobileDriverProvider;

    public AppLauncherExtensions(MobileDriverProvider mobileDriverProvider) {
        this.mobileDriverProvider = mobileDriverProvider;
    }

    public void applicationLaunchOptions(ApplicationLaunchOption applicationLaunchOption, String bundleId) {
        switch (applicationLaunchOption) {
            case LAUNCH -> this.mobileDriverProvider.getAndroidDriver().launchApp();
            case ACTIVATE -> this.mobileDriverProvider.getAndroidDriver().activateApp(bundleId);
            case CLEAR_CASH -> this.mobileDriverProvider.getAndroidDriver().resetApp();
            case IGNORE -> {}
        }
    }
}
