package org.example.Assistant;

import jakarta.persistence.*;
import lombok.*;
import org.example.Assistant.Enum.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Assistant {
    @Id @Column(name = "assistant_id")
    private String id;
    @Column(name="assistant_name")
    private String name;
    @Column(name="assistant_img")
    private String img;
    //512자로 제한하는 거 걸기
    private String description;
    private String instruction;

    private boolean hasFile;

    //튜터 성향
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Enumerated(EnumType.STRING)
    private SpeechLevel speechLevel;

    @Enumerated(EnumType.STRING)
    private Voice voice;

    @Enumerated(EnumType.STRING)
    private AnswerDetail answerDetail;

    @Enumerated(EnumType.STRING)
    private ConversationalStyle conversationalStyle;

    @Enumerated(EnumType.STRING)
    private Emoji emoji;

    @Enumerated(EnumType.STRING)
    private EmotionalExpression emotionalExpression;

    @Enumerated(EnumType.STRING)
    private LanguageMode languageMode;

    @Enumerated(EnumType.STRING)
    private Roleplay roleplay;

    @Enumerated(EnumType.STRING)
    private UseOfTechnicalLanguage useOfTechnicalLanguage;

    public void setHasFileTure(){
        this.hasFile = true;
    }


}
