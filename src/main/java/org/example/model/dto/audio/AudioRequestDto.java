package org.example.model.dto.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AudioRequestDto {
    private String model;
    private String input;
    private String voice;

    public AudioRequestDto(String input, String voice){
        this.voice = voice;
        this.input = input;
    }
}
