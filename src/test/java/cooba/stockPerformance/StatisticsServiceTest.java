package cooba.stockPerformance;

import com.fasterxml.jackson.databind.ObjectMapper;
import cooba.stockPerformance.Database.Entity.StockStatisticsInfo;
import cooba.stockPerformance.Database.Entity.StockTradeInfo;
import cooba.stockPerformance.DBService.StatisticsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cooba.stockPerformance.Constant.Constant.NUM5;

@SpringBootTest
public class StatisticsServiceTest {
    @Mock
    StatisticsService statisticsServiceMock;
    @Spy
    StatisticsService statisticsService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void calculateStatisticsData() {
        StockTradeInfo stockTradeInfo1 = StockTradeInfo.builder()
                .tradingVolume(BigDecimal.valueOf(10))
                .transaction(BigDecimal.valueOf(100))
                .turnover(BigDecimal.ONE)
                .closingPrice(BigDecimal.valueOf(10))
                .build();
        StockTradeInfo stockTradeInfo2 = StockTradeInfo.builder()
                .tradingVolume(BigDecimal.valueOf(10))
                .transaction(BigDecimal.valueOf(110))
                .turnover(BigDecimal.ONE)
                .closingPrice(BigDecimal.valueOf(20))
                .build();
        StockTradeInfo stockTradeInfo3 = StockTradeInfo.builder()
                .tradingVolume(BigDecimal.valueOf(10))
                .transaction(BigDecimal.valueOf(120))
                .turnover(BigDecimal.ONE)
                .closingPrice(BigDecimal.valueOf(30))
                .build();
        StockTradeInfo stockTradeInfo4 = StockTradeInfo.builder()
                .tradingVolume(BigDecimal.valueOf(10))
                .transaction(BigDecimal.valueOf(130))
                .turnover(BigDecimal.ONE)
                .closingPrice(BigDecimal.valueOf(40))
                .build();
        StockTradeInfo stockTradeInfo5 = StockTradeInfo.builder()
                .tradingVolume(BigDecimal.valueOf(10))
                .transaction(BigDecimal.valueOf(140))
                .turnover(BigDecimal.ONE)
                .closingPrice(BigDecimal.valueOf(50))
                .build();
        StockTradeInfo stockTradeInfo6 = StockTradeInfo.builder()
                .tradingVolume(BigDecimal.valueOf(10))
                .transaction(BigDecimal.valueOf(150))
                .turnover(BigDecimal.ONE)
                .closingPrice(BigDecimal.valueOf(60))
                .build();

        List<StockTradeInfo> stockTradeInfoList = Stream.of(stockTradeInfo1, stockTradeInfo2, stockTradeInfo3, stockTradeInfo4, stockTradeInfo5, stockTradeInfo6).collect(Collectors.toList());
        Mockito.when(statisticsServiceMock.getCalculateList(1101, 2022, 7, 2022, 10)).thenReturn(stockTradeInfoList);
        List<StockTradeInfo> calculateList = statisticsServiceMock.getCalculateList(1101, 2022, 7, 2022, 10);
        List<StockStatisticsInfo> statisticsInfoList = calculateList.stream().map(StockStatisticsInfo::transfer).collect(Collectors.toList());

        statisticsService.countNDaysStockTradeInfo(calculateList, statisticsInfoList, NUM5);

//        statisticsInfoList.forEach(stockStatisticsInfo -> {
//            try {
//                System.out.println(objectMapper.writeValueAsString(stockStatisticsInfo));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        });

        assert BigDecimal.valueOf(12).compareTo(statisticsInfoList.get(4).getAvg5dCost()) == 0 : "avg5dCost 計算錯誤";
        assert BigDecimal.valueOf(30).compareTo(statisticsInfoList.get(4).getAvg5dPrice()) == 0 : "avg5dPrice 計算錯誤";
        assert BigDecimal.valueOf(13).compareTo(statisticsInfoList.get(5).getAvg5dCost()) == 0 : "avg5dCost 計算錯誤";
        assert BigDecimal.valueOf(40).compareTo(statisticsInfoList.get(5).getAvg5dPrice()) == 0 : "avg5dPrice 計算錯誤";
    }

    @Test
    public void countLast2Month() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<StatisticsService> clazz = StatisticsService.class;
        Method method = clazz.getDeclaredMethod("countLast2Month", int.class, int.class);
        System.out.println(method.getName());
        method.setAccessible(true);

        Object result = method.invoke(statisticsService, 2022, 9);
        assert !(result instanceof LocalDate localDate) || localDate.getMonthValue() == 8 : "month is not last month";

        Object crossYear = method.invoke(statisticsService, 2022, 1);
        if (crossYear instanceof LocalDate localDate) {
            assert localDate.getYear() == 2021 :"cross year is not last year";
            assert localDate.getMonthValue() == 12 :"cross year month is not last month";
        }
        System.out.println("pass countLast2Month");
    }
}
