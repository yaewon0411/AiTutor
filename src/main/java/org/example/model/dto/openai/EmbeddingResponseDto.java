package org.example.model.dto.openai;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class EmbeddingResponseDto {
    private String model;
    private String object;
    private List<EmbeddingVo> data;
    private UsageVo usage;

    @Data
    public static class EmbeddingVo {
        private String object;
        private float[] embedding;
        private Integer index;
    }

    @Data
    public static class UsageVo {
        private Long promptTokens;
        private Long completionTokens;
        private Long totalTokens;
        private String timestamp;
    }
}