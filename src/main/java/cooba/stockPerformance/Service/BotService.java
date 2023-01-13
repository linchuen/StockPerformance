package cooba.stockPerformance.Service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import cooba.stockPerformance.DBService.CrawlStockcodeService;
import cooba.stockPerformance.DBService.StockMonthDataService;
import cooba.stockPerformance.Database.Entity.StockInfo;
import cooba.stockPerformance.Enums.CommandEnum;
import cooba.stockPerformance.EventHandler.EventPublisher;
import cooba.stockPerformance.Object.DownloadDataRequest;
import cooba.stockPerformance.Utility.DateUtil;
import cooba.stockPerformance.Utility.MongoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
@Slf4j
@Service
public class BotService {
    @Autowired
    MongoUtil mongoUtil;
    @Autowired
    OpenAiService openAiService;
    @Autowired
    CrawlStockcodeService crawlStockcodeService;
    @Autowired
    StockMonthDataService stockMonthDataService;
    @Autowired
    EventPublisher eventPublisher;

    public Method getMessageFromUser(String command) {
        return CommandEnum.getCommandMethodMap().get(command);
    }

    public String botResponse(Method method, String message) throws InvocationTargetException, IllegalAccessException {
        return String.valueOf(method.invoke(this, message));
    }

    public String cmd(String empty) {
        StringBuilder stringBuilder = new StringBuilder();

        for (CommandEnum command : CommandEnum.values()) {
            stringBuilder.append(command.getName()).append(": ").append(command.getDescription()).append("\n");
        }
        return stringBuilder.toString();
    }

    public String ask(String question) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(question)
                .model("text-davinci-003")
                .maxTokens(100)
                .build();
        try{
            List<CompletionChoice> choices = openAiService.createCompletion(completionRequest).getChoices();
            CompletionChoice result = new CompletionChoice();
            result.setText("not find result");
            return choices.stream()
                    .filter(completionChoice -> completionChoice.getFinish_reason().equals("stop"))
                    .findFirst()
                    .orElse(result)
                    .getText();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            mongoUtil.insertException(getClass().getSimpleName(),e.getMessage(),e);
            return "AI encounter error";
        }
    }

    public String build(String empty) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            long count = crawlStockcodeService.getAllStockInfoCount();
            stringBuilder.append("更新前資料庫數量: ").append(count).append("\n");

            crawlStockcodeService.crawlIndustry();
            long afterCount = crawlStockcodeService.getAllStockInfoCount();
            stringBuilder.append("更新股票基本資料成功 數量:").append(afterCount).append("\n");
        } catch (IOException e) {
            stringBuilder.append("建立股票基本資料失敗");
        }
        return stringBuilder.toString();
    }

    public String download(String date) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            LocalDate localDate = DateUtil.getDateByString(date);
            if (localDate.getMonthValue() == LocalDate.now().getMonthValue()) {
                stringBuilder.append("下載月份不應超過當月");
                return stringBuilder.toString();
            }
            eventPublisher.sendTelegramMsg("準備請求");
            stringBuilder.append("開始下載股票月份資料");
            List<StockInfo> stockInfoList = crawlStockcodeService.findAllStockInfo();
            stockInfoList.forEach(stockInfo ->
                    stockMonthDataService.addRequestToDownloadQuene(DownloadDataRequest.builder()
                            .stockInfo(stockInfo)
                            .year(localDate.getYear())
                            .month(localDate.getMonthValue())
                            .build())
            );
        } catch (Exception e) {
            stringBuilder.append("下載股票月份資料失敗");
        }
        return stringBuilder.toString();
    }
}
