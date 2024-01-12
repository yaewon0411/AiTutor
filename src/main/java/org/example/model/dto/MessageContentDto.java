package org.example.model.dto;

import lombok.Data;

@Data
public class MessageContentDto {
    private String type;
    private TextContentDto text;
}
