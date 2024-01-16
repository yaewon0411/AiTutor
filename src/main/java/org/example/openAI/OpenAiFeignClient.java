package org.example.openAI;


import org.example.model.dto.openai.ChatRequestDto;
import org.example.model.dto.openai.EmbeddingRequestDto;
import org.example.model.dto.openai.EmbeddingResponseDto;
import org.example.model.dto.openai.ResponseChatDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

//@FeignClient(name = "OpenAiClient", url = "https://api.openai.com/v1", configuration = OpenAiHeaderConfiguration.class)
@FeignClient(name = "OpenAiClient", url = "https://api.openai.com/v1", configuration = AssistantHeaderConfiguration.class)
public interface OpenAiFeignClient {

    @PostMapping("/embeddings")
    EmbeddingResponseDto createEmbedding(@RequestBody EmbeddingRequestDto embeddingRequestDto);

    @PostMapping("/chat/completions")
    ResponseChatDto chatCompletion(@RequestBody ChatRequestDto chatRequestDto);

    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object uploadFile(@RequestPart MultipartFile file, @RequestPart("purpose") String purpose);

}
