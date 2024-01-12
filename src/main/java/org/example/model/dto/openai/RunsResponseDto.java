package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
public class RunsResponseDto {
    private String id;
    private String object;
    private Long createdAt;
    private String assistantId;
    private String threadId;
    private String status;
    private Long startedAt;
    private Long expiresAt;
    private Long cancelledAt;
    private Long failedAt;
    private Long completedAt;
    private Object lastError;
    private String model;
    private Object instructions;
    private List<Tool> tools;
    private List<String> fileIds;
    private Map<String, Object> metadata;

    @Data
    public static class Tool {
        private String type;
    }
}