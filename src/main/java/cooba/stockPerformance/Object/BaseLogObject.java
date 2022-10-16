package cooba.stockPerformance.Object;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseLogObject {
    private String className;
    private String message;
    private LocalDateTime localDateTime;
    private String dateStr;
}
