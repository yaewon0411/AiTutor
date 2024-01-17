package org.example.Assistant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONPropertyIgnore;

import java.util.List;

@Data
@NoArgsConstructor
public class GptResponseDto {

    private String id;
    public String object;
    public List<ChoiceDto> choices;
}
