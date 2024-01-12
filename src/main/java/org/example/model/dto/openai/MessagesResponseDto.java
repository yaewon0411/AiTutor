package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.dto.MessageContentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
public class MessagesResponseDto {
    private String id;
    private String object;
    private Long createdAt;
    private String threadId;
    private String role;
    private MessageContentDto content;
    private List<String> fileIds;
    private String assistantId;
    private String runId;
    private Map<String, Object> metadata;

}