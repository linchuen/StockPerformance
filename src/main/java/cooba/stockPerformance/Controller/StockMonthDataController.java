package cooba.stockPerformance.Controller;

import cooba.stockPerformance.Database.Entity.StockInfo;
import cooba.stockPerformance.Object.DownloadDataRequest;
import cooba.stockPerformance.Service.CrawlStockcodeService;
import cooba.stockPerformance.Service.StockMonthDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockMonthDataController {
    @Autowired
    StockMonthDataService stockMonthDataService;
    @Autowired
    CrawlStockcodeService crawlStockcodeService;

    @GetMapping("/stock/download/{year}/{month}")
    public ResponseEntity<List<StockInfo>> getAllMonthData(@PathVariable int year, @PathVariable int month) {
        List<StockInfo> stockInfoList = crawlStockcodeService.findAllStockInfo();
        stockInfoList.forEach(stockInfo ->
                stockMonthDataService.addRequestToDownloadQuene(DownloadDataRequest.builder()
                        .stockInfo(stockInfo)
                        .year(year)
                        .month(month)
                        .build())
        );
        return ResponseEntity.ok().body(stockInfoList);
    }
}
