package org.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Locale;
import java.util.function.Predicate;

public class DateTimeUtilExtension extends DateTimeUtil {

    public DateTimeUtilExtension withFutureDate(boolean isFuture) {
        this.isFuture = isFuture;
        return this;
    }

    public DateTimeUtilExtension withStringFormat(String format) {
        this.format = format;
        return this;
    }

    public DateTimeUtilExtension withStartDateAs(Period period) {
        this.period = period;
        return this;
    }

    public DateTimeUtilExtension withLocalTime(LocalTime localTime) {
        this.localTime = localTime;
        return this;
    }

    public DateTimeUtilExtension withStartDateAs(TemporalAdjuster startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate buildDate() {
        try {
            LocalDate localDate = this.setLocalDate(this.startDate, this.isFuture, this.period);
            this.offsetDateTime = OffsetDateTime.of(localDate, this.localTime, ZoneOffset.UTC);
            return this.offsetDateTime.toLocalDate();
        } catch (Exception exception) {
            throw new RuntimeException("generate date error", exception);
        }
    }

    /**
     * toDateString
     * @param format of the date
     * @param date from generated date
     * @param locale for time local option
     * @return String fix date
     */
    public String dateString(String format, LocalDate date, Locale locale) {
        return date.format(DateTimeFormatter.ofPattern(format, locale));
    }

    /**
     * toDateString
     * @param format of the date
     * @param date from generated date
     * @param backUp your increment decrement date option
     * @param locale for time local option
     * @param predicate if this will return true then it will return another date
     * @return String fix date
     */
    public String dateString(String format, LocalDate date, LocalDate backUp, Locale locale, Predicate<DayOfWeek> predicate) {
        if (predicate.test(date.getDayOfWeek())) {
            return date.with(backUp).format(DateTimeFormatter.ofPattern(format, locale));
        } else return date.format(DateTimeFormatter.ofPattern(format, locale));
    }
}
