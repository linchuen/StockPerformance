package cooba.stockPerformance.Database.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class StockTradeInfo {
    @Id
    @Indexed(name = "stockcode_idx")
    private int stockcode;
    @Indexed(name = "date_idx")
    private LocalDate date;
    private BigDecimal tradingVolume;
    private BigDecimal transaction;
    private BigDecimal openingPrice;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private BigDecimal closingPrice;
    private BigDecimal turnover;
}
