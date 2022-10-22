package cooba.stockPerformance.Database.Entity;

import lombok.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@CompoundIndex(name = "compound_sc_date_idx", def = "{ stockcode: 1, data: -1}")
@CompoundIndex(name = "compound_sc_y_m_idx", def = "{ stockcode: 1, year: -1, month: 1}")
public class StockStatisticsInfo {
    @Id
    private String id;
    @Indexed(name = "stockcode_idx")
    private int stockcode;
    @Indexed(name = "date_idx")
    private LocalDate date;
    private int year;
    private int month;
    private BigDecimal avgCost;
    private BigDecimal avgTransaction;
    private BigDecimal avg5dCost;
    private BigDecimal avg10dCost;
    private BigDecimal avg21dCost;
    private BigDecimal avg5dPrice;
    private BigDecimal avg10dPrice;
    private BigDecimal avg21dPrice;

    public static StockStatisticsInfo transfer(StockTradeInfo stockTradeInfo){
        StockStatisticsInfo stockStatisticsInfo = new StockStatisticsInfo();
        try{
            BeanUtils.copyProperties(stockStatisticsInfo, stockTradeInfo);
        }catch (Exception e){
            stockStatisticsInfo.setId(stockStatisticsInfo.getId());
            stockStatisticsInfo.setStockcode(stockStatisticsInfo.getStockcode());
            stockStatisticsInfo.setDate(stockStatisticsInfo.getDate());
            stockStatisticsInfo.setYear(stockStatisticsInfo.getYear());
            stockStatisticsInfo.setMonth(stockStatisticsInfo.getMonth());
        }
        stockStatisticsInfo.setAvgCost(stockTradeInfo.getTransaction().divide(stockTradeInfo.getTradingVolume(),2, RoundingMode.HALF_UP));
        stockStatisticsInfo.setAvgTransaction(stockTradeInfo.getTransaction().divide(stockTradeInfo.getTurnover(),2, RoundingMode.HALF_UP));
        return stockStatisticsInfo;
    }
}
