package org.example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIController {
    private final OpenAIClient openAIClient = new OpenAIClient();

    @GetMapping("/ask")
    public String askOpenAI(@RequestParam String question) {
        return openAIClient.callOpenAI(question);
    }
}