package org.example.Assistant;

import jakarta.persistence.*;
import lombok.*;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;
import org.example.model.dto.ChatDto;
import org.springframework.http.ResponseEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Assistant {
    @Id @Column(name = "assistant_id")
    private String id;
    @Column(name="assistant_name")
    private String name;
    @Column(name="assistant_img")
    private String img;
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

    public void setHasFileTure(){
        this.hasFile = true;
    }
    public void setHasFileFalse(){
        this.hasFile = false;
    }
}
