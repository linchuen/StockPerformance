package cooba.stockPerformance.EventHandler;

import cooba.stockPerformance.Config.TelegramBot;
import cooba.stockPerformance.Event.TeleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class EventNotifier {
    @Autowired
    TelegramBot telegramBot;
    @EventListener
    public void processBlockedListEvent(TeleEvent event) {
        SendMessage message = SendMessage.builder()
                .text(event.getMessage())
                .chatId(telegramBot.getChatID())
                .build();

        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
