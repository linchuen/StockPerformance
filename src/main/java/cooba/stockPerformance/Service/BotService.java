package cooba.stockPerformance.Service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import cooba.stockPerformance.Enums.CommandEnum;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class BotService {
    @Autowired
    OpenAiService openAiService;

    public Method getMessageFromUser(String command) {
        return CommandEnum.getCommandMethodMap().get(command);
    }

    public String botResponse(Method method, String message) throws InvocationTargetException, IllegalAccessException {
        return String.valueOf(method.invoke(this, message));
    }

    public String cmd(String empty) {
        StringBuilder stringBuilder = new StringBuilder();

        for (CommandEnum command : CommandEnum.values()) {
            stringBuilder.append(command.getName()).append(": ").append(command.getDescription()).append("\n");
        }
        return stringBuilder.toString();
    }

    public String ask(String question) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(question)
                .model("text-davinci-003")
                .maxTokens(100)
                .build();
        List<CompletionChoice> choices = openAiService.createCompletion(completionRequest).getChoices();
        CompletionChoice result = new CompletionChoice();
        result.setText("not find result");
        return choices.stream()
                .filter(completionChoice -> completionChoice.getFinish_reason().equals("stop"))
                .findFirst()
                .orElse(result)
                .getText();
    }
}
