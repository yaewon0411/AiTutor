package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ThreadsResponseDto {
    private String id;
    private String object;

    @JsonProperty("created_at")
    private Long createdAt;

    private Object metadata;
}
