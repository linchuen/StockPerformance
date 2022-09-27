package cooba.stockPerformance.Utility;

import cooba.stockPerformance.Constant.MongoCollectionNameEnum;
import cooba.stockPerformance.Object.BaseLogObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MongoUtil {
    @Autowired
    MongoTemplate mongoTemplate;

    public void insertDataExceptionLog(String className, String msg, Exception e) {
        BaseLogObject logObject = BaseLogObject
                .builder()
                .className(className)
                .message(msg)
                .exceptionMsg(e.getMessage())
                .localDateTime(LocalDateTime.now())
                .dateStr(new Date().toString())
                .build();

        mongoTemplate.insert(logObject, MongoCollectionNameEnum.INSERT_DATA_EXCEPTION_LOG.getName());
    }
}
