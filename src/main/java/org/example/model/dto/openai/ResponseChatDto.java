package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
public class ResponseChatDto {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatCompletionChoiceVo> choices;
    private UsageVo usage;

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class UsageVo {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
        private String timestamp;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class ChatCompletionChoiceVo {
        private Integer index;
        private ChatMessageVo message;
        private String finishReason;
    }

    @Data
    public static class ChatMessageVo {
        private String role;
        private String content;
    }
}
