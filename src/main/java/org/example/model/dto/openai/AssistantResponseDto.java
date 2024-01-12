package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AssistantResponseDto {
    private String id;
    private String object;
    private Long createdAt;
    private String name;
    private String description;
    private String model;
    private String instructions;
    private List<Tool> tools;
    @JsonProperty("file_ids")
    private Object fileIds; // Object 타입으로 변경
    private Object metadata; // Object 타입으로 변경

    @Data
    public static class Tool {
        private String type;
    }
}
