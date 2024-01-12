package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorInfoDto {

    private String name;
    private String img;
    private String description;
    private Personality personality;
    private SpeechLevel speechLevel;
    private Voice voice;
}
