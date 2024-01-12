package org.example.model.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileResponseDto {

    private String id;
    private String object;
    private String purpose;
    private String fileName;
    private int bytes;
    @JsonProperty("created_at")
    private String createdAt;
    private String status;

}
