package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;

import java.util.ArrayList;
import java.util.List;

@Data@NoArgsConstructor
public class ModifyRequestDto {
    private String instructions;
    private String model;
    @JsonProperty("file_ids")
    @JsonInclude(JsonInclude.Include.NON_NULL)

    private List<String> fileIds;
    private List<Tool> tools;
    private String description;
    //추가한 파일 경로
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> filePath = new ArrayList<>();


    private String name;


    private Personality personality;
    private SpeechLevel speechLevel;
    private Voice voice;

    @Data
    private class Tool {
        private String type;
    }
}
