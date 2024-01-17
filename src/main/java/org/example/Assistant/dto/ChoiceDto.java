package org.example.Assistant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChoiceDto {
    public int index;
    public MessageDto message;

}
