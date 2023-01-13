package cooba.stockPerformance;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class HttpTest {
    @Autowired
    HttpUtil httpUtil;

    @Test
    public void testGet() throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer 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");
        Response response = httpUtil.httpGet("https://api.openai.com/v1/models/text-davinci-003", Collections.emptyMap(), header);
        assert response.body() != null;
        System.out.println(response.body().string());
        Assertions.assertEquals(200, response.code());
    }

    public static void main(String[] args) {
        OpenAiService service = new OpenAiService("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");
        try {
            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt("這是測試嗎")
                    .model("text-davinci-003")
                    .maxTokens(100)
                    .build();
            service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
        }catch (Exception e){e.printStackTrace();}

    }
}
