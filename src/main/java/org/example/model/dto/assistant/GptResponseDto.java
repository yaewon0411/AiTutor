package org.example.model.dto.assistant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GptResponseDto {

    private String id;
    public String object;
    public List<ChoiceDto> choices;
}
