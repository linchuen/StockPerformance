package cooba.stockPerformance.Object;

import cooba.stockPerformance.Database.Entity.StockInfo;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadDataRequest {
    private StockInfo stockInfo;
    private int year;
    private int month;
}
