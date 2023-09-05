package org.data.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Locale;
import java.util.function.Predicate;

import static java.time.LocalDate.now;

public class DateTimeUtilExtension  {

    public LocalDate buildDate(LocalDate localDate, LocalTime time, ZoneOffset offset) {
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.of(localDate, time, offset);
            return offsetDateTime.toLocalDate();
        } catch (Exception exception) {
            throw new RuntimeException("generate date error", exception);
        }
    }

    /**
     * dateToString
     * @param format of the date
     * @param date from generated date
     * @param backUp your increment decrement date option
     * @param locale for time local option
     * @param predicateBackUp if this will return true then it will return another date
     * @return String fix date
     */
    public String dateToString(String format, LocalDate date, Locale locale, Predicate<DayOfWeek> predicateBackUp, LocalDate backUp) {
        try {
            if (predicateBackUp != null && backUp != null && predicateBackUp.test(date.getDayOfWeek())) {
                return date.with(backUp).format(DateTimeFormatter.ofPattern(format, locale));
            } else return date.format(DateTimeFormatter.ofPattern(format, locale));
        } catch (Exception exception) {
            throw new RuntimeException("generate date error", exception);
        }
    }
}
