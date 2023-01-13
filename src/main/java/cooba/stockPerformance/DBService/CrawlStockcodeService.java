package cooba.stockPerformance.DBService;

import cooba.stockPerformance.Database.Entity.StockInfo;
import cooba.stockPerformance.Database.repository.StockInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CrawlStockcodeService {
    @Autowired
    StockInfoRepository stockInfoRepository;

    public void crawlIndustry(String siteurl) throws IOException {
        List<StockInfo> stockInfoList = new ArrayList<>();

        Document doc = Jsoup.connect(siteurl).get();
        Elements elements = doc.select("tr");
        elements.remove(0);
        elements.remove(0);
        for (Element element : elements) {
            if (element.childNodeSize() == 1) break;
            int stockcode = Integer.parseInt(element.child(0).text().replace(" ", "").split("　")[0]);
            String name = element.child(0).text().split("　")[1];
            String ISINCode = element.child(1).text();
            LocalDate publishDate = LocalDate.parse(element.child(2).text(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String marketType = element.child(3).text();
            String industryType = element.child(4).text();

            StockInfo stockInfo = StockInfo.builder()
                    .stockcode(stockcode)
                    .name(name)
                    .ISINCode(ISINCode)
                    .publishDate(publishDate)
                    .marketType(marketType)
                    .industryType(industryType)
                    .build();

            stockInfoList.add(stockInfo);
        }

        try{
            stockInfoRepository.insert(stockInfoList);
        }catch (Exception e){
            e.printStackTrace();
            log.error("股票基本資料建立錯誤");
        }
    }

    public List<StockInfo> findAllStockInfo(){
        return stockInfoRepository.findAll();
    }
}
