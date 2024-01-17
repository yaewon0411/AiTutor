package org.example.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageContentDto {
    private String type;
    private TextContentDto text;
}
