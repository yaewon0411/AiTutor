package org.example.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.dto.Tool;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssistantCreateRequestDto {
    private String instruction;
    private String name;
    private String description;
    private List<Tool> tools = new ArrayList<>();
    private String model;
    @JsonProperty("file_ids")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> fileIds = new ArrayList<>();
}
