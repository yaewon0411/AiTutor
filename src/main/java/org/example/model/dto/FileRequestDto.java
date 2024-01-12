package org.example.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequestDto {
    private final MultipartFile file;
    private final String purpose;
}
