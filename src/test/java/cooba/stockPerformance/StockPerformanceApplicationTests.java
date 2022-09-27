package cooba.stockPerformance;

import cooba.stockPerformance.Service.CrawlStockcodeService;
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

    @Test
    void contextLoads() {
        try {
            crawlStockcodeService.crawlIndustry("https://isin.twse.com.tw/isin/C_public.jsp?strMode=2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void httpTest(@Autowired OkHttpClient client){
        stockMonthDataService.downloadData(1101,2022,9);

    }

}
