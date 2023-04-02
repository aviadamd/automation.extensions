package org.data.date;

import java.time.*;
import java.time.temporal.TemporalAdjuster;
import static java.time.LocalDate.now;

public class DateTimeUtil {

    protected boolean isFuture = true;
    protected String format = "dd.MM.yyyy";
    protected TemporalAdjuster startDate = null;
    protected Period period = Period.of(0,0,0);
    protected OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.UTC);
    protected LocalDate setLocalDate(TemporalAdjuster startDate, boolean isFuture, Period period) {
        LocalDate localDate;
        if (startDate == null) {
            localDate = isFuture ? now().plus(period) : now().minus(period);
        } else {
            localDate = isFuture ? now().with(startDate).plus(period) : now().with(startDate).minus(period);
        }
        return localDate;
    }
}
