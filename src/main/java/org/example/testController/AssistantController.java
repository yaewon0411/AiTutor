package org.example.testController;

import lombok.RequiredArgsConstructor;
import org.example.Assistant.RealService;
import org.example.Assistant.dto.TutorMessageDto;
import org.example.model.dto.AssistantCreateRequestDto;
import org.example.model.dto.ChatDto;
import org.example.model.dto.CreateRunsRequestDto;
import org.example.model.dto.audio.AudioRequestDto;
import org.example.model.dto.getMessageDto;
import org.example.model.dto.openai.*;
import org.example.Assistant.dto.ModifyRequestDto;
import org.example.service.AssistantService;
import org.example.service.FileService;
import org.example.service.S3Service;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AssistantController {

    private final AssistantService assistantService;
    private final FileService fileService;
    private final S3Service s3Service;
    private final RealService realService;

    @PostMapping("/assistant")
    public ResponseEntity<Object> createAssistant(@RequestBody AssistantCreateRequestDto assistantCreateRequestDto) {
        return assistantService.createAssistant(assistantCreateRequestDto);
    }

    @GetMapping("/assistant")
    public ResponseEntity<Object> getAssistantsList(){
        return assistantService.getAssistantsList();
    }

    @PostMapping("/assistant/{assistantId}/files")
    public ResponseEntity<Object> attachFileToAssistant(@PathVariable String assistantId, @RequestBody AssistantFileRequestDto assistantFileRequestDto){
        return assistantService.attachFileToAssistant(assistantId, assistantFileRequestDto);
    }

    @PostMapping("/assistant/{assistantId}")
    public ResponseEntity<Object> modifyAssistant(@PathVariable String assistantId, @RequestBody ModifyRequestDto modifyRequestDto){
        return assistantService.modifyAssistant(assistantId, modifyRequestDto);
    }

    @PostMapping("/threads")
    public ResponseEntity<Object> createThreads() {
        return assistantService.createThreads();
    }

    @DeleteMapping("/threads/{threadId}")
    public ResponseEntity<Object> deleteThreads(@PathVariable String threadId) {
        return assistantService.deleteThreads(threadId);
    }

    @PostMapping("/messages/{threadId}")
    public ResponseEntity<Object> createMessages(@PathVariable("threadId") String threadId, @RequestBody MessagesRequestDto messagesRequestDto) {
        return assistantService.createMessages(threadId, messagesRequestDto);
    }

    @GetMapping("/messages/list")
    public ResponseEntity<Object> getMessagesList(@RequestParam("threadId") String threadId) {
        return assistantService.getMessagesList(threadId);
    }

    @PostMapping("/runs/{threadId}")
    public ResponseEntity<Object> createRuns(@PathVariable("threadId") String threadId, @RequestBody CreateRunsRequestDto createRunsRequestDto) {
        return assistantService.createRuns(threadId, createRunsRequestDto);
    }

    @GetMapping("/runs/{threadId}")
    public ResponseEntity<Object> getRunList(@PathVariable("threadId") String threadId){
        return assistantService.getRunList(threadId);
    }
    @GetMapping("/runs/{threadId}/run/{runId}")
    public ResponseEntity<Object> searchRun(@PathVariable("threadId")String threadId, @PathVariable("runId")String runId){
        return assistantService.searchRun(threadId, runId);
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
