package cooba.stockPerformance.Object;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LogObject extends BaseLogObject{
    @Builder
    public LogObject(String className, String message, LocalDateTime localDateTime, String dateStr) {
        super(className, message, localDateTime, dateStr);
    }
}
