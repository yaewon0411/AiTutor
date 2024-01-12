package org.example.model.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class FilePathDto {
    private MultipartFile file;
    private String purpose;

    public FilePathDto(){

    }
}
