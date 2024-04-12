package org.example.testController;

import lombok.RequiredArgsConstructor;
import org.example.openAI.file.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<Object> uploadFile(@RequestParam("purpose") String purpose, @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }


}
