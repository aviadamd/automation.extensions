package org.base.mobile;

public enum DriverType {
    ANDROID("ANDROID"),
    IOS("IOS"),
    UNKNOWN("UNKNOWN");

    private final String driverName;
    DriverType(String driverName) { this.driverName = driverName; }
    public String getDriverName() { return this.driverName; }
}
