package cooba.stockPerformance.Service;

import cooba.stockPerformance.Database.repository.StockTradeInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    @Autowired
    private StockTradeInfoRepository stockTradeInfoRepository;

}
