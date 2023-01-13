package cooba.stockPerformance.Task;

import cooba.stockPerformance.DBService.CrawlStockcodeService;
import cooba.stockPerformance.DBService.StockMonthDataService;
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
        crawlStockcodeService.crawlIndustry();
    }

}
