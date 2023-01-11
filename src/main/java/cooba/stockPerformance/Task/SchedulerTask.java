package cooba.stockPerformance.Task;

import cooba.stockPerformance.Service.CrawlStockcodeService;
import cooba.stockPerformance.Service.StockMonthDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SchedulerTask {
    @Autowired
    private StockMonthDataService stockMonthDataService;
    @Autowired
    private CrawlStockcodeService crawlStockcodeService;

    @Scheduled(cron = "0/3 * * * * *")
    public void downloadStockData() {
        stockMonthDataService.pollRequestDownloadData();
    }

    @Scheduled(cron = "0 0 0 1 1,4,7,10 *")
    public void crawlStockBaseInfo() throws IOException {
        crawlStockcodeService.crawlIndustry("https://isin.twse.com.tw/isin/C_public.jsp?strMode=2");
    }

}
