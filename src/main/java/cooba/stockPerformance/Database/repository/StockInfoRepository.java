package cooba.stockPerformance.Database.repository;

import cooba.stockPerformance.Database.Entity.StockInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInfoRepository extends MongoRepository<StockInfo, String> {
}
