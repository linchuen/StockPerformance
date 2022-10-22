package cooba.stockPerformance;

import cooba.stockPerformance.Service.CrawlStockcodeService;
import cooba.stockPerformance.Service.StatisticsService;
import cooba.stockPerformance.Service.StockMonthDataService;
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

    @Test
    void contextLoads() {
        try {
            crawlStockcodeService.crawlIndustry("https://isin.twse.com.tw/isin/C_public.jsp?strMode=2");
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
        statisticsService.calculateStatisticsData(1101,2022,9);
    }

}
