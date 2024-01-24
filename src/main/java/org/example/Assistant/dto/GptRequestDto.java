package org.example.Assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptRequestDto {
    private String model;
    private List<MessageDto> messages;
    private int n;
    private int max_tokens;

}
