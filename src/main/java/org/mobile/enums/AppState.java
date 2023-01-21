package org.mobile.enums;

public enum AppState {
    NOT_INSTALLED("NOT_INSTALLED"),
    NOT_RUNNING("NOT_RUNNING"),
    RUNNING_IN_BACKGROUND_SUSPENDED("RUNNING_IN_BACKGROUND_SUSPENDED"),
    RUNNING_IN_BACKGROUND("RUNNING_IN_BACKGROUND"),
    RUNNING_IN_FOREGROUND("RUNNING_IN_FOREGROUND");

    private final String text;
    AppState(String text) {
        this.text = text;
    }
    public final String getText() { return text; }
}
