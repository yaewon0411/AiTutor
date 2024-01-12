package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

//    private final String apiKey = "YOUR_OPENAI_API_KEY";
//    private final String baseUrl = "https://api.openai.com/v1";
//    private RestTemplate = new RestTemplate();
//
//    public String createAssistant() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//        headers.set("OpenAI-Beta", "assistants=v1");
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("instructions", "You are a personal math tutor. Write and run code to answer math questions.");
//        requestBody.put("model", "gpt-4-1106-preview");
//        requestBody.put("tools", new String[] {"code_interpreter"});
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//        String assistantEndpoint = baseUrl + "/assistants";
//        return restTemplate.postForObject(assistantEndpoint, request, String.class);
//    }
//    public static void main(String[] args) {
//
//        Main assistant = new Main();
//        String assistantId = assistant.createAssistant();
//
//    }
}