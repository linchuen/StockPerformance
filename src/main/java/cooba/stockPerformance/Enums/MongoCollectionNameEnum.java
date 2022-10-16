package cooba.stockPerformance.Enums;

import lombok.Getter;

@Getter
public enum MongoCollectionNameEnum {
    INSERT_DATA_EXCEPTION_LOG("insert_exception_log"),
    LOG("log");

    String name;

    MongoCollectionNameEnum(String name) {
        this.name = name;
    }
}
