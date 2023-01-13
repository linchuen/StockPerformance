package cooba.stockPerformance.Enums;

import cooba.stockPerformance.Service.BotService;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public enum CommandEnum {
    CMD("cmd", "顯示所有可執行指令"),
    ASK("ask", "向機器人提出問題");

    private final String name;
    private final String description;

    CommandEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private static final Map<String, Method> commandMethodMap = new HashMap<>();

    static {
        for (CommandEnum command : CommandEnum.values()) {
            try {
                commandMethodMap.put(command.getName(), BotService.class.getMethod(command.getName(), String.class));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static Map<String, Method> getCommandMethodMap() {
        return commandMethodMap;
    }
}
