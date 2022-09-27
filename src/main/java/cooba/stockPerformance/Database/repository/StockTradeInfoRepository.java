package cooba.stockPerformance.Database.repository;

import cooba.stockPerformance.Database.Entity.StockTradeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTradeInfoRepository  extends MongoRepository<StockTradeInfo, String> {
}
