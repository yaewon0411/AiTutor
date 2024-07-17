package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.assistant.Tool;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@RequiredArgsConstructor
public class AssistantsRequestDto {
    private String model;
    private String name;
    private String instructions;
    private String description;
    private List<Tool> tools;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("file_ids")
    private List<String> fileIds;

    public AssistantsRequestDto(String model, String name, String instructions, @Nullable List<Tool> tools, String description, @Nullable List<String> fileIds){
        this.instructions = instructions;
        this.model = model;
        this.name = name;
        this.tools = tools;
        this.description = description;
        this.fileIds = fileIds;
    }
}