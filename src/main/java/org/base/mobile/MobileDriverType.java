package org.base.mobile;

public class MobileDriverType {
    private final DriverType driverType;
    public MobileDriverType(DriverType driverType) { this.driverType = driverType; }
    public DriverType getDriverType() {
        if (this.driverType != null) {
            if (this.driverType.getDriverName().equals(DriverType.ANDROID.getDriverName())) {
                return DriverType.ANDROID;
            } else if (this.driverType.getDriverName().equals(DriverType.IOS.getDriverName())) {
                return DriverType.IOS;
            } else return DriverType.UNKNOWN;
        }
        return DriverType.UNKNOWN;
    }
}
