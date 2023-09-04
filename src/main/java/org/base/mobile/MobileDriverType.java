package org.base.mobile;

public class MobileDriverType {
    private final String driverType;
    public MobileDriverType(String driverType) { this.driverType = driverType; }

    public DriverType getDriverType() {
        if (this.driverType != null && !this.driverType.isEmpty()) {
            if (this.driverType.equalsIgnoreCase("ANDROID")) {
                return DriverType.ANDROID;
            } else if (this.driverType.equalsIgnoreCase("IOS")) {
                return DriverType.IOS;
            } else return DriverType.UNKNOWN;
        }
        return DriverType.UNKNOWN;
    }
    public enum DriverType {
        ANDROID("ANDROID"),
        IOS("IOS"),
        UNKNOWN("UNKNOWN");

        private final String driverName;
        DriverType(String driverName) { this.driverName = driverName; }
        public String getDriverName() { return this.driverName; }
    }
}
