package cooba.stockPerformance.Utility;

import cooba.stockPerformance.Enums.MongoCollectionNameEnum;
import cooba.stockPerformance.Object.BaseLogObject;
import cooba.stockPerformance.Object.ExceptionLogObject;
import cooba.stockPerformance.Object.LogObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

import static cooba.stockPerformance.Utility.DateUtil.DATETIME_FORMAT;

@Component
public class MongoUtil {
    @Autowired
    MongoTemplate mongoTemplate;

    public void insertDataExceptionLog(String className, String msg, Exception e) {
        String now = DATETIME_FORMAT.format(new Date());

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
        String now = DATETIME_FORMAT.format(new Date());

        BaseLogObject logObject = LogObject
                .builder()
                .className(className)
                .message(msg)
                .localDateTime(LocalDateTime.now())
                .dateStr(now)
                .build();

        mongoTemplate.insert(logObject, MongoCollectionNameEnum.LOG.getName());
    }

    public void insertException(String className, String msg, Exception e) {
        String now = DATETIME_FORMAT.format(new Date());

        BaseLogObject logObject = ExceptionLogObject
                .builder()
                .className(className)
                .message(msg)
                .exceptionMsg(e.getMessage())
                .stackTrace(ExceptionUtils.getStackTrace(e))
                .localDateTime(LocalDateTime.now())
                .dateStr(now)
                .build();

        mongoTemplate.insert(logObject, MongoCollectionNameEnum.EXCEPTION.getName());
    }
}
