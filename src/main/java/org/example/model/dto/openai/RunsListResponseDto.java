package org.example.model.dto.openai;


import lombok.Data;

import java.util.List;

@Data
public class RunsListResponseDto {
    private String object;
    private List<RunsResponseDto> data;

}
