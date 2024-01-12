package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AssistantFileResponseDto {
    private String id;
    private String object;
    private String createdAt;
    @JsonProperty("assistant_id")
    private String assistantId;
}
