package cooba.stockPerformance.Utility;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {
    public static final String NORMAL_FORMAT = "yyyyMMdd";

    public static String getDateString(int year, int month, int day, String type) {
        return LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern(type));
    }
}
