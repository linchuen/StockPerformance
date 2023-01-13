package cooba.stockPerformance.EventHandler;

import cooba.stockPerformance.Event.TeleEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;


    public void setApplicationEventPublisher(@NotNull ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void sendTelegramMsg(String msg) {
        publisher.publishEvent(new TeleEvent(this, msg));
    }
}
