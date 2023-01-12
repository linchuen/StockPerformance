package cooba.stockPerformance.DBService;

import cooba.stockPerformance.Database.Entity.StockStatisticsInfo;
import cooba.stockPerformance.Database.Entity.StockTradeInfo;
import cooba.stockPerformance.Database.repository.StockStatisticsInfoRepository;
import cooba.stockPerformance.Database.repository.StockTradeInfoRepository;
import cooba.stockPerformance.Utility.MongoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static cooba.stockPerformance.Constant.Constant.*;

@Slf4j
@Service
public class StatisticsService {
    @Autowired
    private MongoUtil mongoUtil;
    @Autowired
    private StockTradeInfoRepository stockTradeInfoRepository;
    @Autowired
    private StockStatisticsInfoRepository stockStatisticsInfoRepository;

    public void calculateStatisticsData(int stockcode, int year, int month) {
        LocalDate last2month = countLast2Month(year, month);
        int startYear = last2month.getYear();
        int startMonth = last2month.getMonthValue();
        log.info("Start calculate stock statistics,Stockcode: {}, Year:{} Month:{}", stockcode, year, month);

        List<StockTradeInfo> calculateList = getCalculateList(stockcode, year, month, startYear, startMonth);
        List<StockStatisticsInfo> statisticsInfoList = calculateList.stream().map(StockStatisticsInfo::transfer).collect(Collectors.toList());
        log.info("Be calculated list size:{}", calculateList.size());

        boolean isContainMonth = calculateList.stream().anyMatch(stockTradeInfo -> stockTradeInfo.getMonth() == month);
        if (!isContainMonth) {
            log.warn("list did not contain month data");
            return;
        }

        try {
            if (calculateList.size() > NUM5) {
                countNDaysStockTradeInfo(calculateList, statisticsInfoList, NUM5);
            }
            if (calculateList.size() > NUM10) {
                countNDaysStockTradeInfo(calculateList, statisticsInfoList, NUM10);
            }
            if (calculateList.size() > NUM21) {
                countNDaysStockTradeInfo(calculateList, statisticsInfoList, NUM21);
            }
        } catch (Exception e) {
            String msg = String.format("countNDaysStockTradeInfo error stockcode: %s , date: %d%d", stockcode, year, month);
            log.error("{} countNDaysStockTradeInfo error msg:{}", msg, e.getMessage());
            mongoUtil.insertDataExceptionLog(getClass().getSimpleName(), msg, e);
            return;
        }

        List<StockStatisticsInfo> resultList = statisticsInfoList.stream().filter(stockStatisticsInfo -> stockStatisticsInfo.getMonth() == month).collect(Collectors.toList());
        stockStatisticsInfoRepository.saveAll(resultList);
        log.info("End calculate stock statistics,Stockcode: {}, Year:{} Month:{}", stockcode, year, month);
    }

    public void countNDaysStockTradeInfo(List<StockTradeInfo> calculateList, List<StockStatisticsInfo> resultList, int days) {
        setNDaysStockListValue(calculateList, resultList, days);
        setNDaysStockListValue(resultList, resultList, days);
    }

    private <T> void setNDaysStockListValue(List<T> stockList, List<StockStatisticsInfo> resultList, int days) {
        Class<StockStatisticsInfo> clazz = StockStatisticsInfo.class;
        try {
            if (stockList.isEmpty()) return;
            Method method = null;
            if (stockList.get(0) instanceof StockTradeInfo) {
                method = clazz.getDeclaredMethod("setAvg" + days + "dPrice", BigDecimal.class);
            }
            if (stockList.get(0) instanceof StockStatisticsInfo) {
                method = clazz.getDeclaredMethod("setAvg" + days + "dCost", BigDecimal.class);
            }

            BigDecimal first5Sum = stockList.subList(0, days)
                    .stream()
                    .map(t -> {
                        if (t instanceof StockTradeInfo stockTradeInfo) {
                            return stockTradeInfo.getClosingPrice();
                        }
                        if (t instanceof StockStatisticsInfo stockStatisticsInfo) {
                            return stockStatisticsInfo.getAvgCost();
                        }
                        return null;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal first5AVG = first5Sum.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
            method.invoke(resultList.get(days - 1), first5AVG);

            for (int i = days; i < stockList.size(); i++) {
                T first = stockList.get(i - days);
                T current = stockList.get(i);
                if (first instanceof StockTradeInfo firstStockTradeInfo && current instanceof StockTradeInfo stockTradeInfo) {
                    first5Sum = first5Sum.subtract(firstStockTradeInfo.getClosingPrice()).add(stockTradeInfo.getClosingPrice());
                }
                if (first instanceof StockStatisticsInfo firstStatisticsInfo && current instanceof StockStatisticsInfo statisticsInfo) {
                    first5Sum = first5Sum.subtract(firstStatisticsInfo.getAvgCost()).add(statisticsInfo.getAvgCost());
                }
                BigDecimal resultClosingPrice = first5Sum.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);

                method.invoke(resultList.get(i), resultClosingPrice);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<StockTradeInfo> getCalculateList(int stockcode, int year, int month, int startYear, int startMonth) {
        List<StockTradeInfo> calculateList;
        if (startYear != year) {
            List<StockTradeInfo> lastYearTradeInfoList = stockTradeInfoRepository.findByStockcodeAndYearAndMonthBetween(stockcode, startYear, startMonth - 1, 13);
            List<StockTradeInfo> tradeInfoList = stockTradeInfoRepository.findByStockcodeAndYearAndMonthBetween(stockcode, year, 0, month + 1);
            lastYearTradeInfoList.addAll(tradeInfoList);
            calculateList = lastYearTradeInfoList;
        } else {
            calculateList = stockTradeInfoRepository.findByStockcodeAndYearAndMonthBetween(stockcode, year, startMonth - 1, month + 1);
        }
        return calculateList.stream().sorted(Comparator.comparing(StockTradeInfo::getId)).collect(Collectors.toList());
    }

    private LocalDate countLast2Month(int year, int month) {
        LocalDate inputDate = LocalDate.of(year, month, 1);
        return inputDate.minusMonths(1);
    }
}
