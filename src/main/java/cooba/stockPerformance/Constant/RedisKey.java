package cooba.stockPerformance.Constant;

import org.springframework.stereotype.Component;

@Component
public class RedisKey {
    public static String DOWNLOAD_REQUEST(int stockcode, int year, int month) {
        StringBuilder stringBuilder=new StringBuilder()
                .append("download_request")
                .append(":")
                .append(stockcode)
                .append(":")
                .append(year)
                .append("_")
                .append(month);
        return stringBuilder.toString();
    }
}
