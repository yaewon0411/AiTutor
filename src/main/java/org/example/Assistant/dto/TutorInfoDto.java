package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.Enum.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorInfoDto {

    private String name;
    private String img;
    private String description;
    //튜터 성향
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Personality personality;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SpeechLevel speechLevel;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Voice voice;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AnswerDetail answerDetail;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ConversationalStyle conversationalStyle;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Emoji emoji;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EmotionalExpression emotionalExpression;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LanguageMode languageMode;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Roleplay roleplay;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UseOfTechnicalLanguage useOfTechnicalLanguage;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseLength responseLength;

}
