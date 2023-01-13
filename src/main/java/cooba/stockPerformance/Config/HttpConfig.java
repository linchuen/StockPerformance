package cooba.stockPerformance.Config;

import cooba.stockPerformance.Utility.SSLSocketClientUtil;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

@Configuration
public class HttpConfig {

    @Bean
    public OkHttpClient httpClient(){
        X509TrustManager manager=SSLSocketClientUtil.getX509TrustManager();

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClientUtil.getSocketFactory(manager),manager)
                .hostnameVerifier(SSLSocketClientUtil.getHostnameVerifier())
                .build();
        return client;
    }
}
