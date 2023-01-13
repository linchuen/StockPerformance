package cooba.stockPerformance.Constant;

import org.springframework.stereotype.Component;

public class RedisKey {
    public static String DOWNLOAD_REQUEST(int stockcode, int year, int month) {
        return "download_request" +
                ":" +
                stockcode +
                ":" +
                year +
                "_" +
                month;
    }
}
