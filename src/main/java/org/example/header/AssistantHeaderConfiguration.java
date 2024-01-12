package org.example.header;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantHeaderConfiguration {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    //private static final String OPENAI_API_KEY = "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"; // API 키
    private static final String OPENAI_API_KEY = "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";
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header(AUTHORIZATION_HEADER, "Bearer "+OPENAI_API_KEY);
                template.header("OpenAI-Beta", "assistants=v1");
            }
        };
    }
}
