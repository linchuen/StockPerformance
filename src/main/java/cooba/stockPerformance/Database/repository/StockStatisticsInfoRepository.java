package cooba.stockPerformance.Database.repository;

import cooba.stockPerformance.Database.Entity.StockStatisticsInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockStatisticsInfoRepository extends MongoRepository<StockStatisticsInfo, String> {
    List<StockStatisticsInfo> findByStockcodeAndYearAndMonth(int stockcode, int year, int month);

    List<StockStatisticsInfo> findByStockcodeAndDateBetween(int stockcode, LocalDateTime startTime, LocalDateTime endTime);
}
