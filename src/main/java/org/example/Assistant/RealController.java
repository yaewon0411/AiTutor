package org.example.Assistant;

import lombok.RequiredArgsConstructor;
import org.example.Assistant.dto.*;
import org.example.model.dto.*;
import org.example.model.dto.audio.AudioRequestDto;
import org.example.model.dto.openai.*;
import org.example.service.AssistantService;
import org.example.service.FileService;
import org.example.service.S3Service;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class RealController {

    private final AssistantService assistantService;
    private final FileService fileService;
    private final RealService realService;
    private final S3Service s3Service;

    
    //홈 화면 - 어시스턴트 리스트
    @GetMapping("/")
    public ResponseEntity<Object> home(){
        return ResponseEntity.ok(realService.findAll());
    }

    //어시스턴트 생성 : 등록한 파일이 있으면 먼저 서버에 저장 -> 파일 아이디 프론트에서 저장 -> (파일 있으면) 어시스턴트 code_interpreter로 생성하고 파일 넣기 -> 생성 완료
    @PostMapping("/assistants")
    public ResponseEntity<Object> createAssistant(@ModelAttribute AssistantCreateDto assistantCreateDto) throws IOException {

        //gpt한테 instruction 가공 요청
        String gptInstruction = assistantService.getGptInstruction(assistantCreateDto.getInstruction()).join();

        //enum 타입에 대해서만 필드가 null인지 아닌지 검사
        Map<String, Enum<?>> nonNullFields = assistantService.getNonNullFieldsWhenCreate(assistantCreateDto);

        //튜터 성향 뽑아서 instruction에 넣기
        String setInstruction = assistantService.setInstruction(gptInstruction, nonNullFields);
        boolean hasFile = false;

        AssistantCreateRequestDto AIassistantCreateDto = new AssistantCreateRequestDto();
        AIassistantCreateDto.setName(assistantCreateDto.getName());
        AIassistantCreateDto.setInstruction(setInstruction);
        AIassistantCreateDto.setDescription(assistantCreateDto.getDescription());

        //등록된 파일 있으면 먼저 서버에 저장
        if(assistantCreateDto.getFile1() != null){
            hasFile = true;
            ResponseEntity<Object> response = fileService.uploadFile(assistantCreateDto.getFile1());
            String fileId = fileService.getFileId(response);
            AIassistantCreateDto.getFileIds().add(fileId);
            if(assistantCreateDto.getFile2()!=null){
                ResponseEntity<Object> response2 = fileService.uploadFile(assistantCreateDto.getFile2());
                String fileId2 = fileService.getFileId(response2);
                AIassistantCreateDto.getFileIds().add(fileId2);
            }
        }
        //어시스턴트 생성
        ResponseEntity<Object> assistantObject = assistantService.createAssistant(AIassistantCreateDto);
        String assistantId = assistantService.getAssistantId(assistantObject);

        //이미지 버킷에 저장하고 저장된 경로 반환
        String imgUrl = s3Service.uploadImage(assistantCreateDto.getImgFile());

        //db에 어시스턴트 insert
        Assistant.AssistantBuilder builder =
                Assistant.builder().id(assistantId).name(assistantCreateDto.getName()).img(imgUrl).description(assistantCreateDto.getDescription())
                .instruction(assistantCreateDto.getInstruction()).hasFile(hasFile);

            for (Map.Entry<String, Enum<?>> entry : nonNullFields.entrySet()) {
            String fieldName = entry.getKey();
            Enum<?> fieldValue = entry.getValue();
            try {
                // 필드 이름으로 setter 메서드 이름 구성
                Method method = builder.getClass().getMethod(fieldName, fieldValue.getClass());
                // 메서드 호출해서 값 설정
                method.invoke(builder, fieldValue);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        realService.save(builder.build());

        return ResponseEntity.ok(assistantObject);
    }

    //튜터링 화면 - 상세 정보
    @GetMapping("/assistants/{assistantId}/info")
    public ResponseEntity<Object> tutoringInfo(@PathVariable("assistantId")String assistantId){
        TutorInfoDto res = realService.getTutorInfo(assistantId);
        return ResponseEntity.ok(res);
    }

    //튜터링 화면 - 채팅방 진입
    @GetMapping("/assistants/{assistantId}/chat")
    public ResponseEntity<Object> tutoringPage(@PathVariable("assistantId")String assistantId){
        TutoringPageDto res = realService.findByIdInTutoringPage(assistantId);
        return ResponseEntity.ok(res);
    }
    //스레드 생성하기
    @PostMapping("/assistants/threads")
    public ResponseEntity<Object> createThread(){
        return ResponseEntity.ok(assistantService.createThreads().getBody());
    }
    //메시지 보내고 답변 받기 - 파일(사진 등)으로도 질문하는 경우로 수정
    @PostMapping("/assistants/{threadId}/chat")
    public ResponseEntity<Object> getMessage(@PathVariable("threadId") String threadId, @ModelAttribute getMessageDto getMessageDto
    ){
        //입력한 파일이 있으면
        if(getMessageDto.getFile() != null){
            ResponseEntity<Object> response = fileService.uploadFile(getMessageDto.getFile());
            String fileId = fileService.getFileId(response);
            ArrayList<String> fileIds = new ArrayList<>();
            fileIds.add(fileId);
            //메시지 생성
            assistantService.createMessages(threadId, new MessagesRequestDto("user", getMessageDto.getContent(), fileIds));
        }
        else{
            //메시지 생성
            assistantService.createMessages(threadId, new MessagesRequestDto("user", getMessageDto.getContent()));
        }

        //런 생성
        ResponseEntity<Object> runs = assistantService.createRuns(threadId, new CreateRunsRequestDto(getMessageDto.getAssistantId()));
        //런 아이디 꺼내기
        String runId = assistantService.getRunId(runs);
        //런 실행 상태 추적 후 답변 얻기
        ChatDto chatDto= assistantService.getRunStatusToGetMessage(threadId, runId).join();

        //음성으로 질문한 거라면
        if(getMessageDto.getIsVoice().equals("true")){
            System.out.println("음성 인터페이스 전환");
            String voice = realService.getAssistantVoice(getMessageDto.getAssistantId());
            //ResponseEntity<Object> speech = assistantService.createSpeech(new AudioRequestDto(chatDto.getAnswer()), voice);
            byte[] speech = assistantService.createSpeech2(new AudioRequestDto(chatDto.getAnswer()), voice);
            TutorMessageDto res = new TutorMessageDto(chatDto, speech);
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.ok(chatDto);
    }

    //채팅방 나갈 때 쓰레드 제거
    @DeleteMapping("/assistants/threads/{threadId}")
    public ResponseEntity<Object> deleteThread(@PathVariable("threadId")String threadId){
        return ResponseEntity.ok(assistantService.deleteThreads(threadId));
    }

    //생성한 어시스턴트 수정 화면에 진입 - 저장된 정보 불러오기
    @GetMapping("/assistants/{assistantId}/info/page")
    public ResponseEntity<Object> tutorModifyPage(@PathVariable("assistantId")String assistantId){

        List <String> fileNames = new ArrayList<>();
        Assistant findOne = realService.findById(assistantId);
        if(findOne.isHasFile()){
            ResponseEntity<Object> assistant = assistantService.searchAssistant(assistantId);
            List<String> fileIdList = assistantService.getFileIdList(assistant);
            for (String fileId : fileIdList) {
                String fileName = fileService.getFileName(fileId);
                fileNames.add(fileName);
            }
        }
        TutorModifyDto res = realService.getTutorInfoToModify(assistantId);
        res.setFileNames(fileNames);
        return ResponseEntity.ok(res);
    }


    //파일 없다가 추가하는 경우도 생각해야 함
    // 어시스턴트 수정하기 버튼 클릭 + 어시스턴트에 넣은 파일 삭제하기. 기존 정보에서 달라진 게 없으면 알아서 프론트에서 이거 못 날리게 막도록 가능?
    //어시스턴트 이미지도 수정헀을 때 반영되도록 해야 함
    //어시스턴트 수정
    //1) openAI 수정
    //2) DB 수정
    //변경 된 거 없어도 그대로 값 들고 옴. 필드 삭제되었을 때만 프론트에서 아예 해당 객체 보내지 않음(null)
    @PutMapping("/assistants/{assistantId}/info/page")
    public ResponseEntity<Object> modifyAssistant(@PathVariable("assistantId")String assistantId, @ModelAttribute ModifyRequestDto modifyRequestDto) throws JSONException, IllegalAccessException {

        realService.updateAssistant(assistantId, modifyRequestDto);

        Map<String, Enum<?>> nonNullFields = assistantService.getNonNullFieldsWhenModify(modifyRequestDto);
        String gptInstruction = assistantService.getGptInstruction(modifyRequestDto.getInstruction()).join();
        String setInstruction = assistantService.setInstruction(gptInstruction, nonNullFields);
        modifyRequestDto.setInstruction(setInstruction);

        //파일 변경 검증
        if(modifyRequestDto.getFile1() != null){ //새로 들어오는 파일 경로가 있으면
            ResponseEntity<Object> response = fileService.uploadFile(modifyRequestDto.getFile1());
            System.out.println("it has file1");
            String fileId = fileService.getFileId(response);
                //수정하면서 업로드된 파일 아이디 dto에 넣기
                modifyRequestDto.getFileIds().add(fileId);
                if(modifyRequestDto.getFile2() != null){
                    System.out.println("it has file2");
                    ResponseEntity<Object> response2 = fileService.uploadFile(modifyRequestDto.getFile2());
                    String fileId2 = fileService.getFileId(response2);
                    modifyRequestDto.getFileIds().add(fileId2);
            }
            //hasFile = true 설정
            realService.modifyAssistantHasFileTrue(assistantId);
        } else{ //새로 들어오는 파일이 없으면 -> 삭제된 거 있나 확인

            ResponseEntity<Object> response = assistantService.searchAssistant(assistantId);
            List<String> fileIds = assistantService.getFileIdList(response);
            int fileIdsSize = fileIds.size();

            if(!fileIds.isEmpty()){
                for (String fileId : fileIds) {
                    fileService.deleteFile(fileId);
                    fileIdsSize -= 1;
                }
            }
            if(fileIdsSize == 0){
                realService.modifyAssistantHasFileFalse(assistantId);
            }
        }

        ResponseEntity<Object> res = assistantService.modifyAssistant(assistantId, modifyRequestDto);
        return ResponseEntity.ok(res);
    }

    //사용자가 튜터 이미지를 변경했을 때만 작동
    @PutMapping("/assistants/{assistantId}/info/page/image")
    public ResponseEntity<Object> modifyAssistantImage(@PathVariable("assistantId")String assistantId, @RequestParam("imgFile")MultipartFile file ) throws MalformedURLException {

        Assistant findOne = realService.findById(assistantId);
        String imgPath = findOne.getImg();
        //버킷에서 이미지 삭제
        s3Service.deleteImage(imgPath);
        //버킷에 변경된 사진 업로드
        String img = s3Service.uploadImage(file);
        //튜터의 이미지 수정
        realService.modifyAssistantImg(assistantId, img);

        return ResponseEntity.ok("success");
    }


    //어시스턴트 삭제하기
    @DeleteMapping("/assistants/{assistantId}")
    public ResponseEntity<Object> deleteAssistant(@PathVariable("assistantId") String assistantId) throws MalformedURLException {
        //어시스턴트에 붙은 파일 있는 지 먼저 검사
        ResponseEntity<Object> assistant = assistantService.searchAssistant(assistantId);
        if(assistantService.hasFile(assistant)!=null){
             List<String> fileIdList = assistantService.getFileIdList(assistant);
             for (String fileId : fileIdList) {
                 //OpenAI 서버에 있는 파일 삭제 (어시스턴트에서도 당연히 파일 삭제)
                 fileService.deleteFile(fileId);
             }
        }
        //버킷에서 이미지 삭제
        Assistant findOne = realService.findById(assistantId);
        String img = findOne.getImg();
        s3Service.deleteImage(img);

        //DB에서 어시스턴트 삭제
        realService.deleteAssistant(assistantId);

        //어시스턴트 최종 삭제
        ResponseEntity<Object> res = assistantService.deleteAssistant(assistantId);
        return ResponseEntity.ok(res.getBody());
    }
    //검색
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("keyword")String keyword){
        List<ShowHomeDto> showHomeDtoList = realService.searchByKeyword(keyword);
        return ResponseEntity.ok(showHomeDtoList);
    }


}
