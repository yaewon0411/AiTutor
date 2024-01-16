package org.example.openAI;

import org.example.model.dto.file.FilePathDto;
import org.example.model.dto.file.FileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "OpenAiFileClient", url = "https://api.openai.com/v1", configuration = FileHeaderConfiguration.class)
public interface FileClient {


    //feign 적용한게 아래
    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileResponseDto uploadFile(@RequestBody FilePathDto filePathDto);


    @DeleteMapping("files/{fileId}")
    ResponseEntity<Object> deleteFile(@PathVariable("fileId") String fileId);

    @GetMapping("files/{fileId}")
    ResponseEntity<Object> searchFile(@PathVariable("fileId")String fileId);
}
