package org.component.date;

import lombok.extern.slf4j.Slf4j;
import org.utils.date.DateTimeUtilExtension;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public class DateTimeUtilTest {

    @Test
    public void testDate() {
        String format = "dd MMMM yyyy";
        DateTimeUtilExtension dateUtils = new DateTimeUtilExtension();

        LocalDate localDate = dateUtils.buildDate(LocalDate.now(), LocalTime.now(), ZoneOffset.UTC);
        log.info(localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy'T'HH:mm:ss'Z'")));

        String checkDate1 = dateUtils.dateToString(format, localDate, Locale.ITALIAN, backUp -> backUp.equals(DayOfWeek.FRIDAY) || backUp.equals(DayOfWeek.SATURDAY), LocalDate.now().plusDays(1));
        log.info(checkDate1);

        String checkDate2 = dateUtils.dateToString(format, localDate, Locale.CANADA, null, null);
        log.info(checkDate2);
    }
}
