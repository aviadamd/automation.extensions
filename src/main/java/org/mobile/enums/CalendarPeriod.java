package org.mobile.enums;

import java.util.Calendar;

public enum CalendarPeriod {
    YEAR(Calendar.YEAR),
    MONTH(Calendar.MONTH),
    DAY(Calendar.DATE),
    HOUR(Calendar.HOUR),
    MINUTE(Calendar.MINUTE);

    private final int period;
    CalendarPeriod(int period) { this.period = period; }
    public int getPeriod() { return period; }
}
