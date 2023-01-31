package org.automation.base.mobile.enums.keyboard;

public enum IosKeyBoardActions {
    DELETE("1"),
    CLOSE_KEYBOARD("2");
    private final String code;
    IosKeyBoardActions(String code) {
        this.code = code;
    }
    public String getCode() { return code; }
}
