package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class AssistantCreateDto {

    private Personality personality;
    private SpeechLevel speechLevel;

    private Voice voice;
    private String name;
    private String instruction;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private MultipartFile file1;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private MultipartFile file2;

    private MultipartFile imgFile;

}
