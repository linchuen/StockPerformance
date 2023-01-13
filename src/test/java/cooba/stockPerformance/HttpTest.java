package cooba.stockPerformance;

import cooba.stockPerformance.Utility.HttpUtil;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class HttpTest {
    @Autowired
    HttpUtil httpUtil;

    @Test
    public void testGet() throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer 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");
        Response response = httpUtil.httpGet("https://api.openai.com/v1/models", Collections.emptyMap(), header);
        System.out.println(response.body().string());
        Assertions.assertEquals(200, response.code());
    }
}
