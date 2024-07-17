package org.example.model.dto.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.assistantEnum.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class AssistantCreateRequestDto {

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
    private ResponseLength responseLength;

    @NotNull
    private Voice voice;
    @NotNull
    private String name;
    @NotNull
    private String instruction;
    @NotNull
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private MultipartFile file1;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private MultipartFile file2;
    @NotNull
    private MultipartFile imgFile;

}
