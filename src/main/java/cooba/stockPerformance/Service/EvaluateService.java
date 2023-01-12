package cooba.stockPerformance.Service;

import cooba.stockPerformance.Database.Entity.StockStatisticsInfo;
import cooba.stockPerformance.Database.repository.RateEntityRepository;
import cooba.stockPerformance.Database.repository.StockStatisticsInfoRepository;
import cooba.stockPerformance.Database.Entity.RateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EvaluateService {
    @Autowired
    StockStatisticsInfoRepository stockStatisticsInfoRepository;
    @Autowired
    RateEntityRepository rateEntityRepository;

    public void evaluateStockPerformance(int stockcode, int year, int month, int duration) {
        List<StockStatisticsInfo> statisticsInfoList;
        if (duration == 1) {
            statisticsInfoList = stockStatisticsInfoRepository.findByStockcodeAndYearAndMonth(stockcode, year, month);
        } else {
            LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0, 0).minusSeconds(1);
            LocalDateTime endTime = LocalDateTime.of(year, month + duration, 1, 0, 0, 0);
            statisticsInfoList = stockStatisticsInfoRepository.findByStockcodeAndDateBetween(stockcode, startTime, endTime);
        }

        RateEntity avgCostRate = setGrowthRateList(statisticsInfoList.stream().map(StockStatisticsInfo::getAvgCost).collect(Collectors.toList()));
        fillUpPropertyAndSaveRateEntity(stockcode, year, month, duration,"avg_cost", avgCostRate);
    }

    private RateEntity setGrowthRateList(List<BigDecimal> bigDecimalList) {
        List<Integer> intevalList = new ArrayList<>();
        int inteval = 0;

        List<BigDecimal> standardPointList = new LinkedList<>();
        standardPointList.add(bigDecimalList.get(0));
        Boolean isPositive = null;

        for (int i = 0; i < bigDecimalList.size() - 1; i++) {
            BigDecimal standardPoint = bigDecimalList.get(i);
            BigDecimal compareInfo = bigDecimalList.get(i + 1);
            boolean compareResult = standardPoint.compareTo(compareInfo) > 0;
            if (i == 0) {
                isPositive = compareResult;
            } else {
                inteval++;
                if (isPositive != compareResult) {
                    intevalList.add(inteval);
                    inteval = 0;

                    standardPointList.add(standardPoint);
                    isPositive = compareResult;
                }
            }
        }
        intevalList.add(inteval + 1);
        standardPointList.add(bigDecimalList.get(bigDecimalList.size() - 1));

        List<BigDecimal> rateList = new ArrayList<>();
        for (int i = 1; i < standardPointList.size(); i++) {
            rateList.add(standardPointList.get(i)
                    .subtract(standardPointList.get(i - 1))
                    .divide(BigDecimal.valueOf(intevalList.get(i - 1)), 2, RoundingMode.HALF_UP));
        }
        RateEntity rateEntity = new RateEntity();
        rateEntity.setIntevalList(intevalList);
        rateEntity.setStandardPointList(standardPointList);
        rateEntity.setRateList(rateList);

        return rateEntity;
    }

    private void fillUpPropertyAndSaveRateEntity(int stockcode, int year, int month, int duration, String type, RateEntity rateEntity) {
        String id = Stream.of(stockcode, year, month, duration).map(integer -> String.format("02d", integer)).collect(Collectors.joining("-")) + "-" + type;
        rateEntity.setId(id);
        rateEntity.setStockcode(stockcode);
        rateEntity.setYear(year);
        rateEntity.setMonth(month);
        rateEntity.setDuration(duration);
        rateEntity.setType(type);
        rateEntityRepository.save(rateEntity);
    }
}
