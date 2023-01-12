package cooba.stockPerformance;

import cooba.stockPerformance.Database.Entity.StockStatisticsInfo;
import cooba.stockPerformance.Database.repository.StockStatisticsInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class EvaluateServiceTest {

    @Test
    public void evaluateTest() {
        List<Integer> intevalList = new ArrayList<>();
        int inteval = 0;

        List<Integer> integerList = Stream.of(1, 2, 3, 4, 5, 6, 4, 3, 2, 7, 8, 9, 5, 6, 1, 2, 3).collect(Collectors.toList());
        List<Integer> result = new LinkedList<>();
        result.add(integerList.get(0));
        Boolean isPositive = null;

        for (int i = 0; i < integerList.size() - 1; i++) {
            Integer standardPoint = integerList.get(i);
            Integer compareInfo = integerList.get(i + 1);
            boolean compareResult = standardPoint.compareTo(compareInfo) > 0;
            if (i == 0) {
                isPositive = compareResult;
            } else {
                inteval++;
                if (isPositive != compareResult) {
                    intevalList.add(inteval);
                    inteval = 0;

                    result.add(standardPoint);
                    isPositive = compareResult;
                }
            }
        }
        intevalList.add(inteval + 1);
        result.add(integerList.get(integerList.size() - 1));

        System.out.println(intevalList);
        System.out.println(integerList);

        List<Integer> rateList = new ArrayList<>();
        for (int i = 1; i < result.size(); i++) {
            rateList.add((result.get(i) - result.get(i - 1)) / intevalList.get(i - 1));
        }
        System.out.println(rateList);
        Assertions.assertArrayEquals(result.toArray(), Stream.of(1, 6, 2, 9, 5, 6, 1, 3).toArray());
    }

    @Autowired
    StockStatisticsInfoRepository stockStatisticsInfoRepository;

    @Test
    public void mongoTest() {
        LocalDateTime startTime = LocalDate.of(2022, 9, 1).atStartOfDay();
        LocalDateTime endTime = LocalDate.of(2022, 10, 1).atStartOfDay();
        List<StockStatisticsInfo> statisticsInfoList = stockStatisticsInfoRepository.findByStockcodeAndDateBetween(2330, startTime, endTime);
        boolean containStart=statisticsInfoList.stream().anyMatch(stockStatisticsInfo -> LocalDate.of(2022, 9, 1).equals(stockStatisticsInfo.getDate()));
        boolean containEnd=statisticsInfoList.stream().anyMatch(stockStatisticsInfo -> LocalDate.of(2022, 10, 1).equals(stockStatisticsInfo.getDate()));

        Assertions.assertTrue(containStart);
        Assertions.assertFalse(containEnd);
    }
}
