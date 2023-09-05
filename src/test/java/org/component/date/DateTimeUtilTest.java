package org.component.date;

import lombok.extern.slf4j.Slf4j;
import org.data.date.DateTimeUtilExtension;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.JulianFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Locale;

@Slf4j
public class DateTimeUtilTest {

    @Test
    public void testDate() {
        String format = "dd MMMM yyyy";
        DateTimeUtilExtension dateUtils = new DateTimeUtilExtension();
        LocalDate localDate = dateUtils.buildDate(LocalDate.now().plus(Period.of(1, 0, 0)), LocalTime.now(), ZoneOffset.UTC);
        log.info(localDate.format(DateTimeFormatter.ofPattern(format)));

        String checkDate1 = dateUtils.dateToString(format, localDate, Locale.ITALIAN, backUp -> backUp.equals(DayOfWeek.FRIDAY) || backUp.equals(DayOfWeek.SATURDAY), LocalDate.now().plusDays(1));
        log.info(checkDate1);

        String checkDate2 = dateUtils.dateToString(format, localDate, Locale.CANADA, null, null);
        log.info(checkDate2);
    }
}
