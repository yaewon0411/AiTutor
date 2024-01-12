package org.example.Assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TutorModifyDto {

    private String name;
    private String img;
    private String description;
    private Personality personality;
    private SpeechLevel speechLevel;
    private Voice voice;
    private String instruction;
    private List<String> fileNames = new ArrayList<>();

    public TutorModifyDto(String name, String img, String description, Personality personality, SpeechLevel speechLevel, Voice voice, String instruction) {
        this.name = name;
        this.img = img;
        this.description = description;
        this.personality = personality;
        this.speechLevel = speechLevel;
        this.voice = voice;
        this.instruction = instruction;
    }
}
