package helper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    public static String getRfc1123Format() {
        ZonedDateTime now = ZonedDateTime.now(java.time.ZoneOffset.UTC);
        return now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
