package org.poc.date;

import lombok.extern.slf4j.Slf4j;
import org.data.date.DateTimeUtilExtension;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.Locale;
import java.util.function.Predicate;

@Slf4j
public class DateTimeUtilTest {

    @Test
    public void testDate() {
        String format = "dd MMMM yyyy";
        LocalDate localDate = new DateTimeUtilExtension()
                .withFutureDate(true)
                .withLocalTime(LocalTime.now())
                .withStringFormat(format)
                .withStartDateAs(Period.of(0,0,0))
                .buildDate();

        LocalDate backUp = LocalDate.now().plusDays(1);
        Predicate<DayOfWeek> dayPredicate = day -> day.equals(DayOfWeek.FRIDAY) || day.equals(DayOfWeek.SATURDAY);
        String checkDate1 = new DateTimeUtilExtension().dateString(format, localDate, backUp, Locale.CANADA, dayPredicate);
        log.info(checkDate1);

        String checkDate2 = new DateTimeUtilExtension().dateString(format, localDate, Locale.CANADA);
        log.info(checkDate2);
    }
}
