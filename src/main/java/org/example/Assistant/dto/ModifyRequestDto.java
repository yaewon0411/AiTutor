package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data@NoArgsConstructor
public class ModifyRequestDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
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
    @Nullable
    private MultipartFile file1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private MultipartFile file2;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Personality personality;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SpeechLevel speechLevel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Voice voice;

}
