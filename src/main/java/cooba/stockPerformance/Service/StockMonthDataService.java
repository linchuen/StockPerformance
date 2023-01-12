package cooba.stockPerformance.Service;

import com.opencsv.CSVReader;
import cooba.stockPerformance.Annotation.Statistics;
import cooba.stockPerformance.Constant.RedisKey;
import cooba.stockPerformance.Database.Entity.StockTradeInfo;
import cooba.stockPerformance.Database.repository.StockTradeInfoRepository;
import cooba.stockPerformance.Object.DownloadDataRequest;
import cooba.stockPerformance.Utility.DateUtil;
import cooba.stockPerformance.Utility.HttpUtil;
import cooba.stockPerformance.Utility.MongoUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cooba.stockPerformance.Config.CommonConfig.sdFormat;

@Slf4j
@Service
public class StockMonthDataService {
    @Autowired
    private MongoUtil mongoUtil;
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private StockTradeInfoRepository stockTradeInfoRepository;

    private static final String url = "https://www.twse.com.tw/exchangeReport/STOCK_DAY";
    private static final LinkedBlockingQueue<DownloadDataRequest> blockingQueue = new LinkedBlockingQueue<>();

    public void addRequestToDownloadQuene(DownloadDataRequest request) {
        String redisKey = RedisKey.DOWNLOAD_REQUEST(request.getStockInfo().getStockcode(), request.getYear(), request.getMonth());
        if (Boolean.FALSE.equals(redisTemplate.hasKey(redisKey))) {
            blockingQueue.offer(request);
        }
    }

    public void pollRequestDownloadData() {
        DownloadDataRequest request = blockingQueue.poll();
        if (request != null) {
            downloadData(request.getStockInfo().getStockcode(), request.getYear(), request.getMonth());
        }
    }

    @Statistics
    public void downloadData(int stockcode, int year, int month) {
        String date = DateUtil.getDateString(year, month, 1, DateUtil.NORMAL_FORMAT);
        mongoUtil.insertLog(getClass().getSimpleName(), "Start download stockcode " + stockcode + " data , date: " + year + month);
        try {
            Map<String, String> map = new HashMap<>(Map.of("response", "csv", "date", date, "stockNo", String.valueOf(stockcode)));
            Response response = httpUtil.httpGet(url, map);
            log.info("stock:{} okhttp3 Response:{}", stockcode, response);

            CSVReader csvReader = new CSVReader(Objects.requireNonNull(response.body()).charStream());

            List<String[]> dataRows = csvReader.readAll();
            if (dataRows.isEmpty()) {
                log.warn("data rows is empty");
                return;
            }
            log.info(Arrays.toString(dataRows.get(0)));
            dataRows.remove(1);
            List<StockTradeInfo> stockTradeInfoList = dataRows.stream().filter(strings -> strings.length == 10).map(strings -> {
                String[] dateArr = strings[0].split("/");
                int y = Integer.parseInt(dateArr[0]) + 1911;
                int m = Integer.parseInt(dateArr[1]);
                int d = Integer.parseInt(dateArr[2]);
                LocalDate dataDate = LocalDate.of(y, m, d);
                BigDecimal tradingVolume = new BigDecimal(strings[1]
                        .replace(",", ""));

                BigDecimal transaction = new BigDecimal(strings[2]
                        .replace(",", "")
                        .replace(",", ""));

                BigDecimal openingPrice = new BigDecimal(strings[3]
                        .replace(",", "")
                        .replace("--", "0"));

                BigDecimal highestPrice = new BigDecimal(strings[4]
                        .replace(",", "")
                        .replace("--", "0"));

                BigDecimal lowestPrice = new BigDecimal(strings[5]
                        .replace(",", "")
                        .replace("--", "0"));

                BigDecimal closingPrice = new BigDecimal(strings[6]
                        .replace(",", "")
                        .replace("--", "0"));

                BigDecimal turnover = new BigDecimal(strings[8]
                        .replace(",", ""));

                String id = Stream.of(stockcode, y, m, d).map(integer -> String.format("%02d", integer)).collect(Collectors.joining("-"));

                return StockTradeInfo.builder()
                        .id(id)
                        .stockcode(stockcode)
                        .date(dataDate)
                        .year(y)
                        .month(m)
                        .tradingVolume(tradingVolume)
                        .transaction(transaction)
                        .openingPrice(openingPrice)
                        .highestPrice(highestPrice)
                        .lowestPrice(lowestPrice)
                        .closingPrice(closingPrice)
                        .turnover(turnover)
                        .build();
            }).collect(Collectors.toList());
            log.info("stockTradeInfoList: {}", stockTradeInfoList);

            stockTradeInfoRepository.saveAll(stockTradeInfoList);
            mongoUtil.insertLog(getClass().getSimpleName(), "End download stockcode " + stockcode + " data , date: " + year + month);

            String redisKey = RedisKey.DOWNLOAD_REQUEST(stockcode, year, month);
            String now = sdFormat.format(new Date());
            redisTemplate.boundValueOps(redisKey).setIfAbsent(now, 3, TimeUnit.DAYS);
        } catch (Exception e) {
            String msg = String.format("error stockcode: %s , date: %d%d", stockcode, year, month);
            log.error("{} error msg:{}", msg, e.getMessage());
            mongoUtil.insertDataExceptionLog(getClass().getSimpleName(), msg, e);
        }
    }
}
