package org.automation.base.mobile.enums;

public enum SwipeDirection {
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    DOWN_LARGE("DOWN_LARGE"),
    DOWN("DOWN"),
    UP("UP"),
    UP_LITTLE("UP_LITTLE"),
    UP_LARGE("UP_LARGE"),
    DOWN_LITTLE("DOWN_LITTLE");
    private final String scrollDirection;
    SwipeDirection(String scrollDirection) { this.scrollDirection = scrollDirection; }
    public String getScrollDirection() { return scrollDirection; }
}
