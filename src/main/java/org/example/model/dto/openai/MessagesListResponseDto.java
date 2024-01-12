package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
public class MessagesListResponseDto {
    private String object;
    private List<MessageData> data;
    private String firstId;
    private String lastId;
    private Boolean hasMore;

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class MessageData {
        private String id;
        private String object;
        private Long createdAt;
        private String threadId;
        private String role;
        private List<Content> content;
        private List<String> fileIds;
        private String assistantId;
        private String runId;
        private Map<String, Object> metadata;

        @Data
        public static class Content {
            private String type;
            private Text text;
        }

        @Data
        public static class Text {
            private String value;
            private List<Object> annotations;
        }
    }
}
