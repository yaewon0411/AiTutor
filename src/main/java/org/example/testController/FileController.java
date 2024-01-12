package org.example.testController;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.file.FilePathDto;
import org.example.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /*
    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestBody FilePathDto filePathDto) throws IOException {
        return fileService.uploadFile(filePathDto.getPath());
    }
     */

    @PostMapping("/file")
    public ResponseEntity<Object> uploadFileByFeign(@RequestBody FilePathDto filePathDto) throws IOException {
        return fileService.uploadFileByFeign(filePathDto);
    }

}
