package cooba.stockPerformance.Utility;

import cooba.stockPerformance.Enums.MongoCollectionNameEnum;
import cooba.stockPerformance.Object.BaseLogObject;
import cooba.stockPerformance.Object.ExceptionLogObject;
import cooba.stockPerformance.Object.LogObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MongoUtil {
    @Autowired
    MongoTemplate mongoTemplate;

    public static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public void insertDataExceptionLog(String className, String msg, Exception e) {
        BaseLogObject logObject = ExceptionLogObject
                .builder()
                .className(className)
                .message(msg)
                .exceptionMsg(e.getMessage())
                .localDateTime(LocalDateTime.now())
                .dateStr(sdFormat.format(new Date()))
                .build();

        mongoTemplate.insert(logObject, MongoCollectionNameEnum.INSERT_DATA_EXCEPTION_LOG.getName());
    }

    public void insertLog(String className, String msg) {
        BaseLogObject logObject = LogObject
                .builder()
                .className(className)
                .message(msg)
                .localDateTime(LocalDateTime.now())
                .dateStr(sdFormat.format(new Date()))
                .build();

        mongoTemplate.insert(logObject, MongoCollectionNameEnum.LOG.getName());
    }
}
