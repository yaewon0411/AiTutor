package org.example.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class getMessageDto {
    private String content;
    private String assistantId;
    @Nullable
    private String isVoice;
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filePath;
}
