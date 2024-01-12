package org.example.header;

import feign.Param;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.apache.catalina.webresources.FileResource;
import org.example.model.dto.file.FileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "OpenAiFileClient", url = "https://api.openai.com/v1", configuration = FileHeaderConfiguration.class)
public interface FileClient {

    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileResponseDto uploadFile(@RequestPart("purpose") String purpose, @RequestPart("file") MultipartFile file);

    @DeleteMapping("files/{fileId}")
    ResponseEntity<Object> deleteFile(@PathVariable("fileId") String fileId);

    @GetMapping("files/{fileId}")
    ResponseEntity<Object> searchFile(@PathVariable("fileId")String fileId);
}
