package org.example.model.dto.openai;

import lombok.Data;

import java.util.List;

@Data
public class EmbeddingRequestDto {
    private final String model;
    private final List<String> input;

    public EmbeddingRequestDto(String model, List<String> input) {
        this.model = model;
        this.input = input;
    }
}
