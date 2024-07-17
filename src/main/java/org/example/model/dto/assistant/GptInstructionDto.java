package org.example.model.dto.assistant;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GptInstructionDto {
    @NotNull
    private String instruction;
}
