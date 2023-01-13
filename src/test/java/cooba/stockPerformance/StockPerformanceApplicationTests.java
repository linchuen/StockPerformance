package cooba.stockPerformance;

import cooba.stockPerformance.DBService.CrawlStockcodeService;
import cooba.stockPerformance.Service.EvaluateService;
import cooba.stockPerformance.DBService.StatisticsService;
import cooba.stockPerformance.DBService.StockMonthDataService;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class StockPerformanceApplicationTests {
    @Autowired
    CrawlStockcodeService crawlStockcodeService;
    @Autowired
    StockMonthDataService stockMonthDataService;
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    EvaluateService evaluateService;

    @Test
    void contextLoads() {
        try {
            crawlStockcodeService.crawlIndustry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void downloadTest(@Autowired OkHttpClient client){
        stockMonthDataService.downloadData(6409,2022,9);
    }

    @Test
    void dbTest(){
        statisticsService.calculateStatisticsData(6689,2022,9);
    }

    @Test
    void evaluateTest(){
        evaluateService.evaluateStockPerformance(2330,2022,8,2);
    }

}
