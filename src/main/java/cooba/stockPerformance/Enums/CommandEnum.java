package cooba.stockPerformance.Enums;

import lombok.Getter;

@Getter
public enum CommandEnum {
    CMD("cmd"),
    ASK("ask");

    private String name;

    CommandEnum(String name) {
        this.name = name;
    }
}
