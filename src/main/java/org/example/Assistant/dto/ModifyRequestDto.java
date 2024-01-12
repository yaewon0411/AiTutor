package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data@NoArgsConstructor
public class ModifyRequestDto {
    private String instructions;
    private String model;
    @JsonProperty("file_ids")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object fileIds;
    private List<Tool> tools;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> filePath = new ArrayList<>();

    private String personality;
    private String speechLevel;
    private String voice;

    @Data
    private class Tool {
        private String type;
    }
}
