package cooba.stockPerformance.Database.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
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
@CompoundIndex(name = "compound_sc_date_idx", def = "{ stockcode: 1, data: -1}")
@CompoundIndex(name = "compound_sc_y_m_idx", def = "{ stockcode: 1, year: -1, month: 1}")
public class StockTradeInfo {
    @Id
    private String id;
    @Indexed(name = "stockcode_idx")
    private int stockcode;
    @Indexed(name = "date_idx")
    private LocalDate date;
    private int year;
    private int month;
    private BigDecimal tradingVolume;
    private BigDecimal transaction;
    private BigDecimal openingPrice;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private BigDecimal closingPrice;
    private BigDecimal turnover;
}
