package cooba.stockPerformance.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class CommonConfig {
    public static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    @Bean(name = "now")
    public String now(){
        return sdFormat.format(new Date());
    }
}
