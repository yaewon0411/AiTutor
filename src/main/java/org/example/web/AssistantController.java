package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.assistant.*;
import org.example.model.dto.openai.*;
import org.example.service.OpenAiService;
import org.example.service.FileService;
import org.example.service.AssistantService;
import org.example.service.S3Service;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;


@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/assistants")
public class AssistantController {

    private final OpenAiService openAiService;
    private final FileService fileService;
    private final AssistantService assistantService;
    private final S3Service s3Service;

    
    //홈 화면 - 어시스턴트 리스트
    @GetMapping
    public ResponseEntity<Object> home(){
        return ResponseEntity.ok(assistantService.findAll());
    }

    // gpt한테 instruction울 늘려달라고 요청할 때 사용
    @PostMapping("/instructions")
    public ResponseEntity<Object> createGptInstruction(@RequestBody GptInstructionDto gptInstructionDto){
        String getGptInstruction = openAiService.getGptInstruction(gptInstructionDto.getInstruction()).join();
        return ResponseEntity.ok(getGptInstruction);
    }

    @PostMapping("/gpt4")
    public ResponseEntity<Object> createAssistantWithModel4(@ModelAttribute AssistantCreateRequestDto assistantCreateRequestDto){
        OpenAiAssistantCreateRequestDto openAiAssistantCreateRequestDto = assistantService.createAssistantWithModel4(assistantCreateRequestDto);
        ResponseEntity<Object> assistantObject = openAiService.createAssistantWithModel4(openAiAssistantCreateRequestDto);
        assistantService.save(assistantObject, assistantCreateRequestDto);
        return ResponseEntity.ok(assistantObject);
    }

    //어시스턴트 생성 : 등록한 파일이 있으면 먼저 서버에 저장 -> 파일 아이디 프론트에서 저장 -> (파일 있으면) 어시스턴트 code_interpreter로 생성하고 파일 넣기 -> 생성 완료
    @PostMapping("/gpt3")
    public ResponseEntity<Object> createAssistantWithModel3(@ModelAttribute AssistantCreateRequestDto assistantCreateRequestDto) {
        OpenAiAssistantCreateRequestDto openAiAssistantCreateRequestDto = assistantService.createAssistantWithModel3(assistantCreateRequestDto);
        ResponseEntity<Object> assistantObject = openAiService.createAssistant(openAiAssistantCreateRequestDto);
        assistantService.save(assistantObject, assistantCreateRequestDto);
        return ResponseEntity.ok(assistantObject);
    }
    //튜터링 화면 - 상세 정보
    @GetMapping("/{assistantId}/info")
    public ResponseEntity<Object> tutoringInfo(@PathVariable("assistantId")String assistantId){
        return ResponseEntity.ok(assistantService.getTutorInfo(assistantId));
    }

    //튜터링 화면 - 채팅방 진입
    @GetMapping("/{assistantId}/chats")
    public ResponseEntity<Object> tutoringPage(@PathVariable("assistantId")String assistantId){
        return ResponseEntity.ok(assistantService.findByIdInTutoringPage(assistantId));
    }
    //스레드 생성하기
    @PostMapping("/threads")
    public ResponseEntity<Object> createThread(){
        return ResponseEntity.ok(openAiService.createThreads().getBody());
    }
    //메시지 보내고 답변 받기
    @PostMapping("/{threadId}/chat")
    public ResponseEntity<Object> getMessage(@PathVariable("threadId") String threadId, @ModelAttribute getMessageDto getMessageDto
    ) throws IllegalAccessException{
        return ResponseEntity.ok(openAiService.chatting(threadId, getMessageDto));
    }

    //채팅방 나갈 때 쓰레드 제거
    @DeleteMapping("/threads/{threadId}")
    public ResponseEntity<Object> deleteThread(@PathVariable("threadId")String threadId){
        return ResponseEntity.ok(openAiService.deleteThreads(threadId));
    }

    //생성한 어시스턴트 수정 화면에 진입 - 저장된 정보 불러오기
    @GetMapping("/{assistantId}/info/page")
    public ResponseEntity<Object> tutorModifyPage(@PathVariable("assistantId")String assistantId){
        return ResponseEntity.ok(assistantService.getTutorInfoToModify(assistantId));
    }

    //파일 없다가 추가하는 경우도 생각해야 함
    // 어시스턴트 수정하기 버튼 클릭 + 어시스턴트에 넣은 파일 삭제하기. 기존 정보에서 달라진 게 없으면 알아서 프론트에서 이거 못 날리게 막도록 가능?
    //어시스턴트 이미지도 수정헀을 때 반영되도록 해야 함
    //어시스턴트 수정
    //1) openAI 수정
    //2) DB 수정
    //변경 된 거 없어도 그대로 값 들고 옴. 필드 삭제되었을 때만 프론트에서 아예 해당 객체 보내지 않음(null)
    @PutMapping("/{assistantId}/info/page")
    public ResponseEntity<Object> modifyAssistant(@PathVariable("assistantId")String assistantId, @ModelAttribute ModifyRequestDto modifyRequestDto) throws JSONException, IllegalAccessException {
        return ResponseEntity.ok(assistantService.updateAssistant(assistantId, modifyRequestDto));
    }

    //사용자가 튜터 이미지를 변경했을 때만 작동
    @PatchMapping("/{assistantId}/info/page/image")
    public ResponseEntity<Object> modifyAssistantImage(@PathVariable("assistantId")String assistantId, @RequestParam("imgFile")MultipartFile file ) throws MalformedURLException {
        assistantService.modifyAssistantImg(assistantId, file);
        return ResponseEntity.ok("success");
    }

    //어시스턴트 삭제하기
    @DeleteMapping("/{assistantId}")
    public ResponseEntity<Object> deleteAssistant(@PathVariable("assistantId") String assistantId) throws MalformedURLException {
        return ResponseEntity.ok(assistantService.deleteAssistant(assistantId));
    }
    //검색
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("keyword")String keyword){
        return ResponseEntity.ok(assistantService.searchByKeyword(keyword));
    }

}
