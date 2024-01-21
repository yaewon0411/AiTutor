package org.example.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.example.Assistant.Enum.*;
import org.example.Assistant.dto.*;
import org.example.openAI.AssistantsClient;
import org.example.model.dto.*;
import org.example.model.dto.audio.AudioRequestDto;
import org.example.model.dto.openai.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final AssistantsClient assistantsClient;
    private String model = "gpt-3.5-turbo";
    private String audioModel = "tts-1";

    public ResponseEntity<Object> createAssistant(AssistantCreateRequestDto assistantCreateRequestDto) {
        try {
            Object res = assistantsClient.createAssistants (
                    new AssistantsRequestDto(
                            model,
                            assistantCreateRequestDto.getName(),
                            assistantCreateRequestDto.getInstruction(),
                            Arrays.asList(new Tool("code_interpreter")),
                            assistantCreateRequestDto.getDescription(),
                            assistantCreateRequestDto.getFileIds()
                    )
            );
            return ResponseEntity.ok(res);
        } catch (Exception e) {
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
            System.out.println("res.toString() = " + res.toString());
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
        } catch (Exception e) {
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
    public byte[] createSpeech2(AudioRequestDto audioRequestDto, String voice) {
        byte[] res = assistantsClient.createSpeech(new AudioRequestDto(
                audioModel,
                audioRequestDto.getInput(),
                voice
        ));
        return res;
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
        System.out.println("object.toString() = " + object.toString());
        return object.getString("id");
    }

    public String getRunId(ResponseEntity<Object> runs){

        JSONObject object = new JSONObject(Objects.requireNonNull(runs.getBody()));
        return object.getString("id");
    }

    public ChatDto makeChat(ResponseEntity<Object> messageList){

        ObjectMapper objectMapper = new ObjectMapper();
        MessagesListResponseDto listDto = objectMapper.convertValue(messageList.getBody(), MessagesListResponseDto.class);
        int size = listDto.getData().size();
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


    public String setInstruction(String instruction, Map<String, Enum<?>> nonNullFields) {
        System.out.println("setInstruction 실행!!!");
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
                "gpt-3.5-turbo",
                Arrays.asList(new MessageDto(
                        "system","내가 AI 의 정체성을 설정하는 특정 문장을 주면, " +
                        "너는 이 짧은 문장에 대해 더더욱 살을 붙여서 최대한 긴 문장으로(현재 문자수 기준으로 4배 분량) 재생산해줘." +
                        "이 때 본격적으로 재생산된 특정 문장을 얘기하기 전엔 큰따옴표(\"\")로 감싸줘. "),
                        new MessageDto("user",instruction))
                ,
                1)))
                .thenApply(response ->
                    response.getChoices().get(0).getMessage().getContent());
    }

    public Map<String, Enum<?>> getNonNullFieldsWhenCreate(AssistantCreateDto assistantCreateDto) {

        Map<String, Enum<?>> nonNullFields = new HashMap<>();
        Field[] fields = assistantCreateDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getType().isEnum()){
                try{
                    field.setAccessible(true);
                    Object value = field.get(assistantCreateDto);
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
    public CompletableFuture<ChatDto> getRunStatusToGetMessage(String threadId, String runId){
        return checkRunStatus(threadId, runId).thenApplyAsync(response ->{
            ChatDto chatDto = new ChatDto();
            chatDto.setAnswer(response);
            return chatDto;
        });
    }


}