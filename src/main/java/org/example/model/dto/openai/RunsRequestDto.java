package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RunsRequestDto {
    @JsonProperty("assistant_id")
    private final String assistantId;
    private String model;
    private String instructions;
    private ArrayList<String> tools;
    private Object metadata;

    public RunsRequestDto(String assistantId) {
        this(assistantId, null, null, null, null);
    }

    public RunsRequestDto(String assistantId, String model, String instructions, ArrayList<String> tools, Object metadata) {
        this.assistantId = assistantId;
        this.model = model;
        this.instructions = instructions;
        this.tools = tools;
        this.metadata = metadata;
    }
}
