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

    private static final String OPENAI_API_KEY = "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";

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

    public ResponseEntity<Object> uploadFileByFeign(FilePathDto dto){
        try {
            //String filePath = "C:/Users/admin/Desktop/study/실전! 스프링부트와 JPA 활용2 - API 개발과 성능 최적화v2023-06-14.pdf";
            String filePath = dto.getPath();
            MultipartFile file = convertFileToMultipartFile(filePath);
            Object res = fileClient.uploadFile("assistants", file);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String getFileId(ResponseEntity<String> response){

        JSONObject object = new JSONObject(response.getBody());
        return object.getString("id");
    }

    public ResponseEntity<String> uploadFile(String path) throws IOException {

        //String Path = "C:/Users/admin/Desktop/study/실전! 스프링부트와 JPA 활용2 - API 개발과 성능 최적화v2023-06-14.pdf";

        System.out.println("path = " + path);

        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found at path: " + path);
        }
        FileSystemResource fileResource = new FileSystemResource(new File(path));

        // 헤더 설정 (Authorization 포함)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);

        // 멀티파트 요청 바디 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("purpose", "assistants");

        // Http 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(UPLOAD_URL, HttpMethod.POST, requestEntity, String.class);

        return response;
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
