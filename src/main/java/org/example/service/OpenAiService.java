package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.domain.assistantEnum.*;
import org.example.model.dto.assistant.*;
import org.example.model.dto.audio.AudioRequestDto;
import org.example.model.dto.openai.*;
import org.example.openAI.assistant.AssistantsClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final AssistantsClient assistantsClient;

    private final AssistantService assistantService;
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(OpenAiService.class);
    private String model = "gpt-3.5-turbo";

    private String model4 = "gpt-4-1106-preview";
    private String audioModel = "tts-1";

    private String systemPrompt = "사용자가 '[사용자의 지시문]'라고 입력했습니다. " +
            "원래의 지시문의 내용을 반드시 유지하면서 내용을 더 풍부하고 자세하게 확장해주세요. 명심해주세요 꼭 원래의 지시문 내용을 반드시 유지해야 합니다. " +
            "확장된 내용은 사용자의 원래 지시 사항을 충실히 반영해야 하며, 추가적인 정보, 예시 또는 설명을 포함할 수 있습니다. " +
            "모든 세부사항은 사용자의 지시에 맞게 조정되어야 합니다." +
            "출력하는 문장은 반드시 255자를 넘지 않도록 해주시길 바랍니다. 반드시 255자를 넘기지 말아야 합니다.";

    private int max_tokens = 600;

    public ResponseEntity<Object> createAssistant(OpenAiAssistantCreateRequestDto openAiAssistantCreateRequestDto) {
        try {
            Object res = assistantsClient.createAssistants (
                    new AssistantsRequestDto(
                            model,
                            openAiAssistantCreateRequestDto.getName(),
                            openAiAssistantCreateRequestDto.getInstruction(),
                            Arrays.asList(new Tool("code_interpreter")),
                            openAiAssistantCreateRequestDto.getDescription(),
                            openAiAssistantCreateRequestDto.getFileIds()
                    )
            );
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createAssistantWithModel4(OpenAiAssistantCreateRequestDto openAiAssistantCreateRequestDto){
        try{
            Object res = assistantsClient.createAssistants(
                    new AssistantsRequestDto(
                            model4,
                            openAiAssistantCreateRequestDto.getName(),
                            openAiAssistantCreateRequestDto.getInstruction(),
                            openAiAssistantCreateRequestDto.getTools(),
                            openAiAssistantCreateRequestDto.getDescription(),
                            openAiAssistantCreateRequestDto.getFileIds()
                    )
            );
            return ResponseEntity.ok(res);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> searchAssistant(String assistantId){
        try{
            Object res = assistantsClient.searchAssistant(assistantId);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> modifyAssistant(String assistantId, ModifyRequestDto assistantModifyRequestDto) {
        try{
            Object res = assistantsClient.modifyAssistant(assistantId,
                    new AssistantModifyRequestDto(
                            assistantModifyRequestDto.getInstruction(),
                            model,
                            assistantModifyRequestDto.getFileIds(),
                            assistantModifyRequestDto.getDescription(),
                            Arrays.asList(new Tool("code_interpreter")),
                            assistantModifyRequestDto.getName()
                    ));
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //TODO : 얘는 어시스턴트 gpt 4 로 수정하는 거 적용하는 애
    public ResponseEntity<Object> modifyAssistantWithModel4(String assistantId, ModifyRequestDto assistantModifyRequestDto) {
        try{
            Object res = assistantsClient.modifyAssistant(assistantId,
                    new AssistantModifyRequestDto(
                            assistantModifyRequestDto.getInstruction(),
                            model4,
                            assistantModifyRequestDto.getFileIds(),
                            assistantModifyRequestDto.getDescription(),
                            assistantModifyRequestDto.getTools(),
                            assistantModifyRequestDto.getName()
                    ));
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteAssistantFile(String assistantId, String fileId){
        try{
            return assistantsClient.deleteAssistantFile(assistantId, fileId);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteAssistant(String assistantId){
        try{
            return assistantsClient.deleteAssistant(assistantId);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createThreads() {
        try {
            Object res = assistantsClient.createThreads();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteThreads(String threadId) {
        try {
            Object res = assistantsClient.deleteThreads(threadId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createMessages(String threadId, MessagesRequestDto messagesRequestDto) {
        try {
            MessagesResponseDto res= assistantsClient.createMessages(
                    threadId,
                    messagesRequestDto);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getMessagesList(String threadId) {
        try {
            Object res = assistantsClient.getMessagesList(threadId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createRuns(String threadId, CreateRunsRequestDto createRunsRequestDto) {
        try {
            Object res = assistantsClient.createRuns(
                    threadId,
                    new RunsRequestDto(createRunsRequestDto.getAssistantId())
            );
            return ResponseEntity.ok(res);
        } 
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getRunList(String threadId){
        try{
            Object res = assistantsClient.getRunList(threadId);

            return ResponseEntity.ok(res);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> searchRun(String threadId, String runId) {
        try{
            Object res = assistantsClient.searchRun(threadId, runId);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> createSpeech(AudioRequestDto audioRequestDto, String voice) {
        byte[] res= assistantsClient.createSpeech(new AudioRequestDto(
                audioModel,
                audioRequestDto.getInput(),
                voice
        ));
        String path = "C:/Users/admin/Desktop/speech.mp3";
        return saveFile(res, path);
    }

    //그냥 바로 바이트 배열로 반환하는 것도 프론트랑 테스트 해볼 것
    public String createSpeech2(AudioRequestDto audioRequestDto, String voice) {
        byte[] res = assistantsClient.createSpeech(new AudioRequestDto(
                audioModel,
                audioRequestDto.getInput(),
                voice
        ));
        return Base64.getEncoder().encodeToString(res);
    }

    public ResponseEntity<Object> saveFile(byte[] data, String path){
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            fileOutputStream.write(data);
            System.out.println("Audio file saved successfully at: " + path);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("success");
    }

    public ResponseEntity<Object> getAssistantsList() {
        try {
            Object res = assistantsClient.getAssistantsList();
            return ResponseEntity.ok(res);
        }catch(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> attachFileToAssistant(String assistantId, AssistantFileRequestDto assistantFileRequestDto) {
        try{
            Object res = assistantsClient.attachFileToAssistant(assistantId, assistantFileRequestDto);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getAssistantId(ResponseEntity<Object> assistantObject) {
        JSONObject object = new JSONObject(Objects.requireNonNull(assistantObject.getBody()));
        return object.getString("id");
    }

    public String getRunId(ResponseEntity<Object> runs) throws IllegalAccessException {
        JSONObject object = new JSONObject(Objects.requireNonNull(runs.getBody()));
        return object.getString("id");
    }

    public ChatDto makeChat(ResponseEntity<Object> messageList){

        ObjectMapper objectMapper = new ObjectMapper();
        MessagesListResponseDto listDto = objectMapper.convertValue(messageList.getBody(), MessagesListResponseDto.class);
        ChatDto chatDto = new ChatDto();

        List<MessagesListResponseDto.MessageData> data = listDto.getData();
        String answer = data.get(0).getContent().get(0).getText().getValue();
        chatDto.setAnswer(answer);

        return chatDto;
    }

    public Object hasFile(ResponseEntity<Object> assistant) {;
        JSONObject jsonObject = new JSONObject((assistant.getBody()));
        return jsonObject.get("fileIds");
    }
    public List<String> getFileIdList(ResponseEntity<Object> assistant){

        JSONObject object = new JSONObject(Objects.requireNonNull(assistant.getBody()));
        JSONArray fileIdsArray = object.getJSONArray("fileIds");

        List<String> fileIds = new ArrayList<>();
        for (int i = 0; i < fileIdsArray.length(); i++) {
            fileIds.add(fileIdsArray.getString(i));
        }
        return fileIds;
    }
    public String getModel(ResponseEntity<Object> assistant){
        JSONObject object = new JSONObject(assistant.getBody());
        return object.getString("model");
    }

    public String setInstruction(String instruction, Map<String, Enum<?>> nonNullFields) {
        String res = instruction + "\n *지침 : ";
        for (Enum<?> value : nonNullFields.values()) {
            if(value.getClass().equals(AnswerDetail.class)){
                res += "당신은 답변하는 내용을 "+value+" 식으로 제공하셔야 합니다.\n";
            }
            else if(value.getClass().equals(ConversationalStyle.class)){
                res += "당신은 사용자와 "+value.toString()+" 스타일로 대화합니다.\n";
            }
            else if(value.getClass().equals(Emoji.class)){
                res += "당신은 답변에 이모티콘을 "+value.toString()+"합니다.\n";
            }
            else if(value.getClass().equals(EmotionalExpression.class)){
                res += "당신은 사용자의 입력에 대해 "+value.toString()+"하게 판단해서 답변을 생성하셔야 합니다.\n";
            }
            else if(value.getClass().equals(LanguageMode.class)){
                if(value == LanguageMode.SingleLanguage){
                    res += "당신은 처음 사용자가 입력한 언어로만 답변을 생성하셔야 합니다.\n";
                }
                else{
                    res += "당신은 다중 언어로 답변을 생성하실 수 있습니다.";
                }
            }
            else if(value.getClass().equals(Personality.class)){
                res += "당신은 답변할 때 "+value.toString()+"하게 답변해주셔야 합니다.\n";
            }
            else if(value.getClass().equals(Roleplay.class)){
                res += "당신은 또한 "+value.toString()+"스타일로 답변합니다. 이 역할에 몰입해서 답변해주시기 바랍니다!\n";
            }
            else if(value.getClass().equals(SpeechLevel.class)){
                res += "당신은 "+value.toString() +"하게 사용자에게 답변해줍니다.\n";
            }
            else if(value.getClass().equals(ResponseLength.class)){
                if(value.toString().equals("Short")) res += "답변할 때 반드시 62자 이내로 답변해야 합니다.\n";
                else if(value.toString().equals("Medium")) res += "답변할 때 반드시 84~105자 이내로 답변해야 합니다.\n";
                else res += "답변할 때 반드시 105자 이상으로 답변해야 합니다.\n";
            }
            else if(value.getClass().equals(UseOfTechnicalLanguage.class)){
                if(value == UseOfTechnicalLanguage.Use){
                    res += "답변할 때 제발 전문적인 용어를 사용해서 설명해주시기 바랍니다!\n";
                }
                else{
                    res += "답변할 때 제발 전문 용어가 아닌 쉬운 말로 설명해주시기 바랍니다!\n";
                }
            }
        }
        res += "* 만약 사용자가 대화 시 파일을 첨부한다면, 이는 utf-8로 인코딩된 파일일 것입니다. 이를 감안해주시기 바랍니다.";

        return res;
    }

    public String setInstructionWithModel4(boolean hasFile, String instruction, Map<String, Enum<?>> nonNullFields) {
        String res = instruction + "\n *지침 : ";
        if(hasFile) res += "사용자가 첨부된 파일과 관련된 이야기를 하고, 당신이 이에 대해 답변할 때 반드시 답변의 출처는 포함하지 않고 대답해주세요\n ";
        for (Enum<?> value : nonNullFields.values()) {
            if(value.getClass().equals(AnswerDetail.class)){
                res += "당신은 답변하는 내용을 "+value+" 식으로 제공하셔야 합니다.\n";
            }
            else if(value.getClass().equals(ConversationalStyle.class)){
                res += "당신은 사용자와 "+value.toString()+" 스타일로 대화합니다.\n";
            }
            else if(value.getClass().equals(Emoji.class)){
                res += "당신은 답변에 이모티콘을 "+value.toString()+"합니다.\n";
            }
            else if(value.getClass().equals(EmotionalExpression.class)){
                res += "당신은 사용자의 입력에 대해 "+value.toString()+"하게 판단해서 답변을 생성하셔야 합니다.\n";
            }
            else if(value.getClass().equals(LanguageMode.class)){
                if(value == LanguageMode.SingleLanguage){
                    res += "당신은 처음 사용자가 입력한 언어로만 답변을 생성하셔야 합니다.\n";
                }
                else{
                    res += "당신은 다중 언어로 답변을 생성하실 수 있습니다.";
                }
            }
            else if(value.getClass().equals(Personality.class)){
                res += "당신은 답변할 때 "+value.toString()+"하게 답변해주셔야 합니다.\n";
            }
            else if(value.getClass().equals(Roleplay.class)){
                res += "당신은 또한 "+value.toString()+"스타일로 답변합니다. 이 역할에 몰입해서 답변해주시기 바랍니다!\n";
            }
            else if(value.getClass().equals(SpeechLevel.class)){
                res += "당신은 "+value.toString() +"하게 사용자에게 답변해줍니다.\n";
            }
            else if(value.getClass().equals(ResponseLength.class)){
                if(value.toString().equals("Short")) res += "답변할 때 반드시 62자 이내로 답변해야 합니다.\n";
                else if(value.toString().equals("Medium")) res += "답변할 때 반드시 84~105자 이내로 답변해야 합니다.\n";
                else res += "답변할 때 반드시 105자 이상으로 답변해야 합니다.\n";
            }
            else if(value.getClass().equals(UseOfTechnicalLanguage.class)){
                if(value == UseOfTechnicalLanguage.Use){
                    res += "답변할 때 제발 전문적인 용어를 사용해서 설명해주시기 바랍니다!\n";
                }
                else{
                    res += "답변할 때 제발 전문 용어가 아닌 쉬운 말로 설명해주시기 바랍니다!\n";
                }
            }
        }
        res += "* 만약 사용자가 대화 시 파일을 첨부한다면, 이는 utf-8로 인코딩된 파일일 것입니다. 이를 감안해주시기 바랍니다.";

        return res;
    }

    public CompletableFuture<String> getGptInstruction(String instruction){
        return CompletableFuture.supplyAsync(()-> assistantsClient.createChat(new GptRequestDto(
                model,
                Arrays.asList(new MessageDto(
                        "system",systemPrompt),
                        new MessageDto("user","사용자의 지시문 : "+ instruction))
                ,
                1,
                        max_tokens)))
                .thenApply(response ->
                    response.getChoices().get(0).getMessage().getContent());
    }

    public Map<String, Enum<?>> getNonNullFieldsWhenCreate(AssistantCreateRequestDto assistantCreateRequestDto) {

        Map<String, Enum<?>> nonNullFields = new HashMap<>();
        Field[] fields = assistantCreateRequestDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getType().isEnum()){
                try{
                    field.setAccessible(true);
                    Object value = field.get(assistantCreateRequestDto);
                    if(value != null){
                        nonNullFields.put(field.getName(), (Enum<?>)value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return nonNullFields;
    }

    public Map<String, Enum<?>> getNonNullFieldsWhenModify(ModifyRequestDto modifyRequestDto) {

        Map<String, Enum<?>> nonNullFields = new HashMap<>();
        Field[] fields = modifyRequestDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getType().isEnum()){
                try{
                    field.setAccessible(true);
                    Object value = field.get(modifyRequestDto);
                    if(value != null){
                        nonNullFields.put(field.getName(), (Enum<?>)value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return nonNullFields;
    }
    //런을 계속 실행시켜서 status를 확인하고 이게 completed로 바뀌면
    //메시지 리스트의 해당 메시지 답변 내용을 반환
    public CompletableFuture<String> checkRunStatus(String threadId, String runId){
        return CompletableFuture.supplyAsync(() -> assistantsClient.searchRun(threadId, runId))
                .thenComposeAsync(response -> {
                    String status = response.getStatus().toString();
                    if(status.equals("completed")){
                        return CompletableFuture.supplyAsync(() -> {
                            ResponseEntity<Object> res = getMessagesList(threadId);
                            return makeChat(res).getAnswer();
                        });
                    } else{
                        return checkRunStatus(threadId, runId);
                    }
                });
    }
    public CompletableFuture<ChatDto> getRunStatusToGetMessage(String threadId, String runId) {
        return checkRunStatus(threadId, runId).thenApplyAsync(response -> {
            ChatDto chatDto = new ChatDto();
            String ver1 = response.replaceAll("\r\n", "<br>");
            String ver2 = ver1.replaceAll("\n", "<br>");
            chatDto.setAnswer(ver2);
            return chatDto;
        });
    }


    public Object chatting(String threadId, getMessageDto getMessageDto) throws IllegalAccessException{

        //입력한 파일이 있으면
        if(getMessageDto.getFile() != null){
            ResponseEntity<Object> response = fileService.uploadFile(getMessageDto.getFile());
            String fileId = fileService.getFileId(response);
            ArrayList<String> fileIds = new ArrayList<>();
            fileIds.add(fileId);
            //메시지 생성
            createMessages(threadId, new MessagesRequestDto("user", getMessageDto.getContent(), fileIds));
        }
        else{
            //메시지 생성
            createMessages(threadId, new MessagesRequestDto("user", getMessageDto.getContent()));
        }
        //런 생성
        ResponseEntity<Object> runs = createRuns(threadId, new CreateRunsRequestDto(getMessageDto.getAssistantId()));
        //System.out.println("runs.toString() = " + runs.toString());
        //런 아이디 꺼내기
        String runId = getRunId(runs);
        //런 실행 상태 추적 후 답변 얻기
        ChatDto chatDto =  getRunStatusToGetMessage(threadId, runId).join();

        //음성으로 질문한 거라면
        if(getMessageDto.getIsVoice().equals("true")){
            System.out.println("음성 인터페이스 전환");
            String voice = assistantService.getAssistantVoice(getMessageDto.getAssistantId());
            String speech = createSpeech2(new AudioRequestDto(chatDto.getAnswer()), voice);
            TutorMessageDto res = new TutorMessageDto(chatDto, speech);
            return ResponseEntity.ok(res);
        }
        return chatDto;
    }
}