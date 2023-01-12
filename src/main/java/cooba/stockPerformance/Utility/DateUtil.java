package cooba.stockPerformance.Utility;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {
    public static final String NORMAL_FORMAT = "yyyyMMdd";
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public static String getDateString(int year, int month, int day) {
        return LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern(NORMAL_FORMAT));
    }

    public static String getDateString(int year, int month, int day, String type) {
        return LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern(type));
    }
}
