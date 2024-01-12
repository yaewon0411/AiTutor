package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.header.AssistantsClient;
import org.example.model.dto.*;
import org.example.model.dto.audio.AudioRequestDto;
import org.example.model.dto.openai.*;
import org.example.Assistant.dto.ModifyRequestDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final AssistantsClient assistantsClient;
    private String assistantModel = "gpt-3.5-turbo";

    public ResponseEntity<Object> createAssistant(AssistantCreateRequestDto assistantCreateRequestDto) {
        try {
            Object res = assistantsClient.createAssistants (
                    new AssistantsRequestDto(
                            assistantModel,
                            assistantCreateRequestDto.getName(),
                            assistantCreateRequestDto.getInstruction(),
                            assistantCreateRequestDto.getTools(),
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
                            assistantModel,
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

    public ResponseEntity<Object> createMessages(String threadId, MessagesRequestDto messageRequestDto) {
        try {
            Object res = assistantsClient.createMessages(
                    threadId,
                    new MessagesRequestDto(
                            "user",
                            messageRequestDto.getContent(),
                            messageRequestDto.getFileIds()
                    )
            );
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

    public ResponseEntity<Object> getRun(String threadId, String runId) {
        try{
            Object res = assistantsClient.getRun(threadId, runId);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> createSpeech(AudioRequestDto audioRequestDto) {
        byte[] res= assistantsClient.createSpeech(new AudioRequestDto(
                "tts-1",
                audioRequestDto.getInput(),
                "alloy"
        ));
        String path = "C:/Users/admin/Desktop/speech.mp3";
        return saveFile(res, path);
    }

    //그냥 바로 바이트 배열로 반환하는 것도 프론트랑 테스트 해볼 것
    public ResponseEntity<byte[]> createSpeech2(AudioRequestDto audioRequestDto) {
        byte[] res = assistantsClient.createSpeech(new AudioRequestDto(
                "tts-1",
                audioRequestDto.getInput(),
                audioRequestDto.getVoice()
        ));
        return ResponseEntity.ok(res);
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
        System.out.println("answer = " + answer);
        chatDto.setAnswer(answer);

        return chatDto;
    }

    public Object hasFile(ResponseEntity<Object> assistant) {;
        JSONObject jsonObject = new JSONObject((assistant.getBody()));
        System.out.println("jsonObject.toString() = " + jsonObject.toString());
        return jsonObject.get("file_ids");
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



    //어시스턴트 name, description, instruction, files_ids만 내보내면 됨
    public void getAssistantInfo(ResponseEntity<Object> assistant) {

        JSONObject object = new JSONObject(Objects.requireNonNull(assistant.getBody()));

        //만드는 중!!!
    }
    //
    public String getAssistantName(ResponseEntity<Object> assistantObject) {
        JSONObject object = new JSONObject(Objects.requireNonNull(assistantObject.getBody()));
        return object.get("name").toString();
    }

    public String getAssistantDescription(ResponseEntity<Object> assistantObject) {
        JSONObject object = new JSONObject(Objects.requireNonNull(assistantObject.getBody()));
        return object.get("description").toString();
    }

    public String setInstruction(String instruction, String personality, String speechLevel) {
        return instruction + "\n답변할 때 "+personality+"하게 답변 해주세요.\n 당신은 또한 "+speechLevel+"하게 답변합니다.";
    }
}