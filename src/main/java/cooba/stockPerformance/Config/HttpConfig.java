package cooba.stockPerformance.Config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpConfig {

    @Bean
    public OkHttpClient httpClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.MILLISECONDS)
                .build();
        return client;
    }
}
