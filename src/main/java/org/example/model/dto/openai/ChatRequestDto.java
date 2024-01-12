package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequestDto {
    private String model;
    private List<ChatMessageVo> messages;

    @JsonProperty("max_tokens")
    private int maxTokens;

    private double temperature;
    private boolean stream;

    @Data
    public static class ChatMessageVo {
        private String role;
        private String content;
    }
}
