package cooba.stockPerformance.Utility;

import cooba.stockPerformance.Enums.MongoCollectionNameEnum;
import cooba.stockPerformance.Object.BaseLogObject;
import cooba.stockPerformance.Object.ExceptionLogObject;
import cooba.stockPerformance.Object.LogObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MongoUtil {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    @Qualifier("now")
    private String now;

    public void insertDataExceptionLog(String className, String msg, Exception e) {
        BaseLogObject logObject = ExceptionLogObject
                .builder()
                .className(className)
                .message(msg)
                .exceptionMsg(e.getMessage())
                .stackTrace(ExceptionUtils.getStackTrace(e))
                .localDateTime(LocalDateTime.now())
                .dateStr(now)
                .build();

        mongoTemplate.insert(logObject, MongoCollectionNameEnum.INSERT_DATA_EXCEPTION_LOG.getName());
    }

    public void insertLog(String className, String msg) {
        BaseLogObject logObject = LogObject
                .builder()
                .className(className)
                .message(msg)
                .localDateTime(LocalDateTime.now())
                .dateStr(now)
                .build();

        mongoTemplate.insert(logObject, MongoCollectionNameEnum.LOG.getName());
    }
}
