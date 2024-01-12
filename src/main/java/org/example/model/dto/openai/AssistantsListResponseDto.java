package org.example.model.dto.openai;

import lombok.Data;

import java.util.List;

@Data
public class AssistantsListResponseDto {
    private String object;
    private List<AssistantResponseDto> data;
}
