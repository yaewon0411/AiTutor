package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.example.header.FileClient;
import org.example.model.dto.file.FilePathDto;
import org.example.model.dto.file.FileResponseDto;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileClient fileClient;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String UPLOAD_URL = "https://api.openai.com/v1/files";

    @Value("${openai.key}")
    private String OPENAI_API_KEY;

    public ResponseEntity<Object> searchFile(String fileId){
        try{
            ResponseEntity<Object> res = fileClient.searchFile(fileId);
            return res;
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteFile(String fileId){
        try{
            return fileClient.deleteFile(fileId);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> uploadFile(MultipartFile file){
        try {
            Object res = fileClient.uploadFile(new FilePathDto(file, "assistants"));
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String getFileId(ResponseEntity<Object> response){

        JSONObject object = new JSONObject(response.getBody());
        return object.getString("id");
    }

    public MultipartFile convertFileToMultipartFile(String filePath) throws IOException{
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile("file", fileName, contentType, content);
    }

    public String getFileName(String fileId) {

        ResponseEntity<Object> file = searchFile(fileId);
        LinkedHashMap<String, String> body = (LinkedHashMap<String, String>) file.getBody();
        return body.get("filename");
    }


    private class MockMultipartFile implements MultipartFile {
        public MockMultipartFile(String file, String fileName, String contentType, byte[] content) {
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getOriginalFilename() {
            return null;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return new byte[0];
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {

        }
    }
}


