package org.example.model.dto.assistant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowHomeDto {
    public String name;
    public String img;
    public String assistantId;
}
