package cooba.stockPerformance.Database.repository;

import cooba.stockPerformance.Database.Entity.RateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateEntityRepository extends MongoRepository<RateEntity, String> {
}
