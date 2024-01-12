package org.example.model.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilePathDto {
    @JsonProperty("path")
    private String path;

    public FilePathDto(){

    }
}
