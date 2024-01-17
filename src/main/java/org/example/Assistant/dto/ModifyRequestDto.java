package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.Enum.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data@NoArgsConstructor
public class ModifyRequestDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private String instruction;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String model;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private List<String> fileIds = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private List<Tool> tools = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private String description;
    //추가한 파일 경로

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private MultipartFile file1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private MultipartFile file2;

    //Enum

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private Personality personality;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private SpeechLevel speechLevel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private AnswerDetail answerDetail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private ConversationalStyle conversationalStyle;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private Emoji emoji;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private EmotionalExpression emotionalExpression;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private LanguageMode languageMode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private Roleplay roleplay;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private UseOfTechnicalLanguage useOfTechnicalLanguage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private Voice voice;

}
