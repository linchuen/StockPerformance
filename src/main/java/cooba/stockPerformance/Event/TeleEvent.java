package cooba.stockPerformance.Event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TeleEvent extends ApplicationEvent {
    private final String message;

    public TeleEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
