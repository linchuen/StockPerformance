package cooba.stockPerformance.Controller;

import cooba.stockPerformance.Config.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
public class TelegramBotController {
    @Autowired
    TelegramBot telegramBot;

    @GetMapping("/telegram/test")
    public ResponseEntity<SendMessage> teletest() {
        SendMessage message = SendMessage.builder()
                .text("test")
                .chatId("-1001666069142")
                .build();

        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(message);
    }
}
