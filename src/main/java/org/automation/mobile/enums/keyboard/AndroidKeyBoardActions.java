package org.automation.mobile.enums.keyboard;

import io.appium.java_client.android.nativekey.AndroidKey;

public enum AndroidKeyBoardActions {
    DELETE(AndroidKey.DEL),
    CLOSE_KEYBOARD(AndroidKey.SEARCH);
    private final AndroidKey code;
    AndroidKeyBoardActions(AndroidKey code) {
        this.code = code;
    }
    public AndroidKey getCode() { return code; }
}
