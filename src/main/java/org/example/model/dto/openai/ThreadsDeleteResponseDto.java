package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ThreadsDeleteResponseDto {
    private String id;
    private String object;
    private boolean deleted;
}