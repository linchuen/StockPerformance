package cooba.stockPerformance.Object;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseLogObject {
    private String className;
    private String message;
    private String exceptionMsg;
    private LocalDateTime localDateTime;
    private String dateStr;
}
