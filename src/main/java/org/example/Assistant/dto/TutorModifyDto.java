package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.Enum.*;

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


    public TutorModifyDto(String name, String img, String description, Voice voice, String instruction) {
        this.name = name;
        this.img = img;
        this.description = description;
        this.voice = voice;
        this.instruction = instruction;
    }
}
