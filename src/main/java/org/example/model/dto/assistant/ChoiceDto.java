package org.example.model.dto.assistant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChoiceDto {
    public int index;
    public MessageDto message;

}
