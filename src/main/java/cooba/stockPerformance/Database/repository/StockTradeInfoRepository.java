package cooba.stockPerformance.Database.repository;

import cooba.stockPerformance.Database.Entity.StockTradeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTradeInfoRepository extends MongoRepository<StockTradeInfo, String> {
    List<StockTradeInfo> findByStockcodeAndYearAndMonth(int stockcode, int year, int month);

    List<StockTradeInfo> findByStockcodeAndYearAndMonthBetween(int stockcode, int year, int startMonth,int endMonth);
}
