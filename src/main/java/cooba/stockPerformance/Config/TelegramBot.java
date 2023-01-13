package cooba.stockPerformance.Config;

import cooba.stockPerformance.Service.BotService;
import cooba.stockPerformance.Utility.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;

@Component
public class TelegramBot extends TelegramLongPollingBot {


    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.token}")
    private String bot_name;
    @Value("${telegram.chatID}")
    private String chatID;
    @Autowired
    MongoUtil mongoUtil;
    @Autowired
    BotService botService;

    @Override
    public String getBotUsername() {
        return this.bot_name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    public String getChatID() {
        return this.chatID;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            String[] msgGroup = update.getMessage().getText().split(" ");
            String command = msgGroup[0];
            Method method = botService.getMessageFromUser(command);
            if (method == null) return;

            String resopnse = botService.botResponse(method, msgGroup.length > 1 ? msgGroup[1] : "");
            SendMessage message = SendMessage.builder()
                    .text(resopnse)
                    .chatId(chatID)
                    .build();
            execute(message);
        } catch (Exception e) {
            mongoUtil.insertException(getClass().getSimpleName(), "Exception on telegram update", e);
        }
    }
}
