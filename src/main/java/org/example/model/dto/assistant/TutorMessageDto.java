package org.example.model.dto.assistant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorMessageDto {
    private ChatDto chatDto;
    private String speech;
}
