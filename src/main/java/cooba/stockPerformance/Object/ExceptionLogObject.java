package cooba.stockPerformance.Object;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionLogObject extends BaseLogObject {
    private String exceptionMsg;
    private String stackTrace;

    @Builder
    public ExceptionLogObject(String className, String message, LocalDateTime localDateTime, String dateStr, String exceptionMsg, String stackTrace) {
        super(className, message, localDateTime, dateStr);
        this.exceptionMsg = exceptionMsg;
        this.stackTrace = stackTrace;
    }
}
