package cooba.stockPerformance.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import cooba.stockPerformance.Database.Entity.StockTradeInfo;
import cooba.stockPerformance.Database.repository.StockInfoRepository;
import cooba.stockPerformance.Database.repository.StockTradeInfoRepository;
import cooba.stockPerformance.Utility.DateUtil;
import cooba.stockPerformance.Utility.HttpUtil;
import cooba.stockPerformance.Utility.MongoUtil;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockMonthDataService {
    @Autowired
    MongoUtil mongoUtil;
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private StockTradeInfoRepository stockTradeInfoRepository;

    private static final String url = "https://www.twse.com.tw/exchangeReport/STOCK_DAY";

    public void downloadData(int stockcode, int year, int month) {
        String date = DateUtil.getDateString(year, month, 1, DateUtil.NORMAL_FORMAT);

        try {
            Map<String, String> map = new HashMap<>(Map.of("response", "csv", "date", date, "stockNo", String.valueOf(stockcode)));
            Response response = httpUtil.httpGet(url, map);
            CSVReader csvReader = new CSVReader(response.body().charStream());

            List<String[]> dataRows = csvReader.readAll();
            dataRows.remove(1);
            List<StockTradeInfo> stockTradeInfoList = dataRows.stream().filter(strings -> strings.length > 10).map(strings -> {
                String[] dateArr = strings[0].split("/");
                int y = Integer.parseInt(dateArr[0]) + 1911;
                int m = Integer.parseInt(dateArr[1]);
                int d = Integer.parseInt(dateArr[2]);
                LocalDate dataDate = LocalDate.of(y, m, d);
                BigDecimal tradingVolume = new BigDecimal(strings[1].replace(",", ""));
                BigDecimal transaction = new BigDecimal(strings[2].replace(",", ""));
                BigDecimal openingPrice = new BigDecimal(strings[3].replace("--", "0"));
                BigDecimal highestPrice = new BigDecimal(strings[4].replace("--", "0"));
                BigDecimal lowestPrice = new BigDecimal(strings[5].replace("--", "0"));
                BigDecimal closingPrice = new BigDecimal(strings[6].replace("--", "0"));
                BigDecimal turnover = new BigDecimal(strings[8].replace(",", ""));

                return StockTradeInfo.builder()
                        .date(dataDate)
                        .tradingVolume(tradingVolume)
                        .transaction(transaction)
                        .openingPrice(openingPrice)
                        .highestPrice(highestPrice)
                        .lowestPrice(lowestPrice)
                        .closingPrice(closingPrice)
                        .turnover(turnover)
                        .build();
            }).collect(Collectors.toList());

            stockTradeInfoRepository.saveAll(stockTradeInfoList);
        } catch (IOException | CsvException e) {
            String msg = String.format("error stockcode: %s , date: %d%d", stockcode, year, month);
            mongoUtil.insertDataExceptionLog(getClass().getSimpleName(), msg, e);
        }
    }
}
