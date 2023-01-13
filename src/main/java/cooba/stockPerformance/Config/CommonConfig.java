package cooba.stockPerformance.Config;

import cooba.stockPerformance.Utility.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class CommonConfig {

    @Bean(name = "now")
    public String now(){
        return DateUtil.DATETIME_FORMAT.format(new Date());
    }
}
