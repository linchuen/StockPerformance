package cooba.stockPerformance.Database.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class StockInfo {
    @Id
    @Indexed(name = "stockcode_idx")
    private int stockcode;
    private String name;
    private String ISINCode;
    @Indexed(name = "publish_date_idx")
    private LocalDate publishDate;
    private String marketType;
    @Indexed(name = "industry_type_idx")
    private String industryType;
}
