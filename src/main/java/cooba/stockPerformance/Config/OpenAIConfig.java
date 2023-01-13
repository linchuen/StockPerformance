package cooba.stockPerformance.Config;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.model.Model;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class OpenAIConfig {
    @Bean
    public OpenAiService setOpenAI(@Value("${openai.token}") String token) {
        return new OpenAiService(token);
    }

    public static Map<String, Model> getAIModel(OpenAiService openAiService) {
        return openAiService.listModels().stream().collect(Collectors.toMap(Model::getId, Function.identity()));
    }

}
