package org.example.model.dto.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.assistantEnum.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorModifyDto {

    private String name;
    private String img;
    private String description;
    private Voice voice;
    private String instruction;
    private List<String> fileNames = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> fileIds = new ArrayList<>();

    private String model;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Personality personality;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SpeechLevel speechLevel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AnswerDetail answerDetail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ConversationalStyle conversationalStyle;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Emoji emoji;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EmotionalExpression emotionalExpression;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LanguageMode languageMode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Roleplay roleplay;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UseOfTechnicalLanguage useOfTechnicalLanguage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseLength responseLength;


}
