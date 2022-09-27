package cooba.stockPerformance.Constant;

import lombok.Getter;

@Getter
public enum MongoCollectionNameEnum {
    INSERT_DATA_EXCEPTION_LOG("insertDataExceptionLog");

    String name;

    MongoCollectionNameEnum(String name) {
        this.name = name;
    }
}
