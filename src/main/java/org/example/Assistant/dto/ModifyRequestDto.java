package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;

import java.util.ArrayList;
import java.util.List;

@Data@NoArgsConstructor
public class ModifyRequestDto {
    private String instruction;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String model;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> fileIds = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Tool> tools = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    //추가한 파일 경로
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> filePath = new ArrayList<>();


    private String name;


    private Personality personality;
    private SpeechLevel speechLevel;
    private Voice voice;

}
