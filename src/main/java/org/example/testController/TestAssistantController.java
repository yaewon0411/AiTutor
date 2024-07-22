package org.example.testController;

import lombok.RequiredArgsConstructor;
import org.example.service.AssistantService;
import org.example.model.dto.openai.OpenAiAssistantCreateRequestDto;
import org.example.model.dto.assistant.CreateRunsRequestDto;
import org.example.model.dto.openai.*;
import org.example.model.dto.assistant.ModifyRequestDto;
import org.example.service.OpenAiService;
import org.example.service.FileService;
import org.example.service.S3Service;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class TestAssistantController {

    private final OpenAiService openAiService;
    private final FileService fileService;
    private final S3Service s3Service;
    private final AssistantService assistantService;

    @PostMapping("/assistant")
    public ResponseEntity<Object> createAssistant(@RequestBody OpenAiAssistantCreateRequestDto openAiAssistantCreateRequestDto) {
        return openAiService.createAssistant(openAiAssistantCreateRequestDto);
    }

    @GetMapping("/assistant")
    public ResponseEntity<Object> getAssistantsList(){
        return openAiService.getAssistantsList();
    }

    @PostMapping("/assistant/{assistantId}/files")
    public ResponseEntity<Object> attachFileToAssistant(@PathVariable String assistantId, @RequestBody AssistantFileRequestDto assistantFileRequestDto){
        return openAiService.attachFileToAssistant(assistantId, assistantFileRequestDto);
    }

    @PostMapping("/assistant/{assistantId}")
    public ResponseEntity<Object> modifyAssistant(@PathVariable String assistantId, @RequestBody ModifyRequestDto modifyRequestDto){
        return openAiService.modifyAssistant(assistantId, modifyRequestDto);
    }

    @PostMapping("/threads")
    public ResponseEntity<Object> createThreads() {
        return openAiService.createThreads();
    }

    @DeleteMapping("/threads/{threadId}")
    public ResponseEntity<Object> deleteThreads(@PathVariable String threadId) {
        return openAiService.deleteThreads(threadId);
    }

    @PostMapping("/messages/{threadId}")
    public ResponseEntity<Object> createMessages(@PathVariable("threadId") String threadId, @RequestBody MessagesRequestDto messagesRequestDto) {
        return openAiService.createMessages(threadId, messagesRequestDto);
    }

    @GetMapping("/messages/list")
    public ResponseEntity<Object> getMessagesList(@RequestParam("threadId") String threadId) {
        return openAiService.getMessagesList(threadId);
    }

//    @PostMapping("/runs/{threadId}")
//    public ResponseEntity<Object> createRuns(@PathVariable("threadId") String threadId, @RequestBody CreateRunsRequestDto createRunsRequestDto) {
//        return openAiService.createRuns(threadId, createRunsRequestDto);
//    }

    @GetMapping("/runs/{threadId}")
    public ResponseEntity<Object> getRunList(@PathVariable("threadId") String threadId){
        return openAiService.getRunList(threadId);
    }
    @GetMapping("/runs/{threadId}/run/{runId}")
    public ResponseEntity<Object> searchRun(@PathVariable("threadId")String threadId, @PathVariable("runId")String runId){
        return openAiService.searchRun(threadId, runId);
    }

    // 나중에 프론트로 바이트 배열 자체를 넘길 때 사용. service 함수 수정할 것
//    @PostMapping(value = "/audio", produces = "audio/mpeg")
//    public ResponseEntity<byte[]> createSpeech(@RequestBody AudioRequestDto audioRequestDto){
//
//        return assistantService.createSpeech2(audioRequestDto);
//        /*
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"")
//                .contentType(MediaType.parseMediaType("audio/mpeg"))
//                .body(assistantService.createSpeech2(audioRequestDto));
//         */
//    }


}
