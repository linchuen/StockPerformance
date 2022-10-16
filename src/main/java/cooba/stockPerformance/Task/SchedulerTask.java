package cooba.stockPerformance.Task;

import cooba.stockPerformance.Service.StockMonthDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerTask {
    @Autowired
    private StockMonthDataService stockMonthDataService;

    @Scheduled(cron = "0/3 * * * * *")
    public void downloadStockData() {
        stockMonthDataService.pollRequestDownloadData();
    }

}
