package cooba.stockPerformance.AOP;

import cooba.stockPerformance.DBService.StatisticsService;
import cooba.stockPerformance.DBService.StockMonthDataService;
import cooba.stockPerformance.Object.DownloadDataRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopAspect {
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    StockMonthDataService stockMonthDataService;

    @AfterReturning(pointcut = "@annotation(cooba.stockPerformance.Annotation.Statistics)", returning = "request")
    public void addStatistics(JoinPoint joinPoint, Object request) {
        if (request instanceof DownloadDataRequest dataRequest) {
            statisticsService.calculateStatisticsData(dataRequest.getStockInfo().getStockcode(), dataRequest.getYear(), dataRequest.getMonth());
        }
        stockMonthDataService.sendFinishMsgToTelegram();
    }
}
