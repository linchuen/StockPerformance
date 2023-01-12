package cooba.stockPerformance.AOP;

import cooba.stockPerformance.DBService.StatisticsService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopAspect {
    @Autowired
    private StatisticsService statisticsService;

    @After("execution(public void cooba.stockPerformance.DBService.StockMonthDataService.downloadData(int,int,int))")
    public void addStatistics(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        int stockcode = (int) args[0];
        int year = (int) args[1];
        int month = (int) args[2];
        statisticsService.calculateStatisticsData(stockcode, year, month);
    }
}
