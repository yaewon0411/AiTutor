package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.example.domain.assistantEnum.Voice;
import org.example.model.dto.assistant.*;
import org.example.domain.assistant.Assistant;
import org.example.domain.assistant.AssistantRepository;
import org.example.model.dto.audio.AudioRequestDto;
import org.example.model.dto.openai.OpenAiAssistantCreateRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssistantService {
    private final AssistantRepository assistantRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final OpenAiService openAiService;
    private final S3Service s3Service;


    @Transactional(readOnly = true)
    public List<ShowHomeDto> findAll(){
        return assistantRepository.findAll().stream()
                .map(assistant -> new ShowHomeDto(assistant.getName(), assistant.getImg(), assistant.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Assistant findById(String assistantId){
        Optional<Assistant> findOne = assistantRepository.findById(assistantId);
        if(findOne.isPresent()) return findOne.get();
        else throw new EntityNotFoundException("Assistant not found with id: " + assistantId);
    }

    @Transactional(readOnly = true)
    public TutoringPageDto findByIdInTutoringPage(String assistantId) {
        Assistant findOne = findById(assistantId);
        return new TutoringPageDto(findOne.getName(), findOne.getDescription(), findOne.getImg());
    }

    @Transactional(readOnly = true)
    public TutorInfoDto getTutorInfo(String assistantId){
        Assistant findOne = findById(assistantId);
        return new TutorInfoDto(findOne.getName(), findOne.getImg(), findOne.getDescription(), findOne.getPersonality(),findOne.getSpeechLevel(),findOne.getVoice(),
                findOne.getAnswerDetail(), findOne.getConversationalStyle(), findOne.getEmoji(), findOne.getEmotionalExpression(), findOne.getLanguageMode(),
                findOne.getRoleplay(), findOne.getUseOfTechnicalLanguage(), findOne.getResponseLength());
    }

    //튜터 수정 화면에 뿌려지는 정보
    @Transactional(readOnly = true)
    public TutorModifyDto getTutorInfoToModify(String assistantId){

        List <String> fileNames = new ArrayList<>();
        Assistant findOne = findById(assistantId);
        List<String> fileIds = new ArrayList<>();

        if(findOne.isHasFile()){
            ResponseEntity<Object> assistant = openAiService.searchAssistant(assistantId);
            List<String> fileIdList = openAiService.getFileIdList(assistant);
            fileIds = fileIdList;
            for (String fileId : fileIdList) {
                String fileName = fileService.getFileName(fileId);
                fileNames.add(fileName);
            }
        }

        TutorModifyDto tutorModifyDto = modelMapper.map(findOne, TutorModifyDto.class);
        tutorModifyDto.setFileNames(fileNames);
        tutorModifyDto.setFileIds(fileIds);
        tutorModifyDto.setModel(openAiService.getModel(openAiService.searchAssistant(assistantId)));

        return tutorModifyDto;
    }
    @Transactional(readOnly = true)
    public String getAssistantVoice(String assistantId){
        Assistant findOne = findById(assistantId);
        Voice voice = findOne.getVoice();
        if(voice.toString().equals("Female")) return "shimmer";
        else return "onyx";
    }
    public TutorMessageDto voiceChatting(getMessageDto getMessageDto, ChatDto chatDto) { //음성으로 질문한 거라면
        System.out.println("음성 인터페이스 전환");
        String voice = getAssistantVoice(getMessageDto.getAssistantId());
        String speech = openAiService.createSpeech2(new AudioRequestDto(chatDto.getAnswer()), voice);
        return new TutorMessageDto(chatDto, speech);
    }

    public void modifyAssistantImg(String assistantId, MultipartFile file) throws MalformedURLException {

        Assistant findOne = findById(assistantId);
        String imgPath = findOne.getImg();
        //버킷에서 이미지 삭제
        s3Service.deleteImage(imgPath);
        //버킷에 변경된 사진 업로드
        String img = s3Service.uploadImage(file);
        //튜터의 이미지 수정
        assistantRepository.updateAssistantImgById(assistantId, img);
    }

    public ResponseEntity<Object> deleteAssistant(String assistantId) throws MalformedURLException {
        //어시스턴트에 붙은 파일 있는 지 먼저 검사
        ResponseEntity<Object> assistant = openAiService.searchAssistant(assistantId);
        if(openAiService.hasFile(assistant)!=null){
            List<String> fileIdList = openAiService.getFileIdList(assistant);
            for (String fileId : fileIdList) {
                //OpenAI 서버에 있는 파일 삭제 (어시스턴트에서도 당연히 파일 삭제)
                fileService.deleteFile(fileId);
            }
        }
        //버킷에서 이미지 삭제
        Assistant findOne = findById(assistantId);
        String img = findOne.getImg();
        s3Service.deleteImage(img);

        //DB에서 어시스턴트 삭제
        assistantRepository.deleteAssistantById(assistantId);
        //어시스턴트 최종 삭제
        ResponseEntity<Object> res = openAiService.deleteAssistant(assistantId);
        return res;
    }


    public void modifyAssistantHasFileTrue(String assistantId) {
        Assistant findOne = findById(assistantId);
        findOne.setHasFileTure();
    }

    public void modifyAssistantHasFileFalse(String assistantId){
        Assistant findOne = findById(assistantId);
        findOne.setHasFileFalse();
    }

    //TODO -> 일단 전체 엔티티 들고와서 DTO로 매핑해서 반환하는 거 성능 테스트 하고
    //TODO -> 그 뒤에, 레퍼지토리에서 바로 DTO로 매핑해서 가져오는 거 성능 테스트
    public List<ShowHomeDto> searchByKeyword(String keyword){
        List<Assistant> assistants = assistantRepository.searchByKeyword(keyword);

        return assistants.stream()
                        .map(assistant -> new ShowHomeDto(assistant.getName(), assistant.getImg(), assistant.getId()))
                .collect(Collectors.toList());
    }


    public ResponseEntity<Object> updateAssistant(String assistantId, ModifyRequestDto modifyRequestDto) throws IllegalAccessException {

        Map<String, Enum<?>> nonNullFields = getNonNullFieldsWhenModify(modifyRequestDto);

        //파일 변경 검증
        if(modifyRequestDto.getFile1() != null){ //새로 들어오는 파일 경로가 있으면
            ResponseEntity<Object> response = fileService.uploadFile(modifyRequestDto.getFile1());
            String fileId = fileService.getFileId(response);
            //수정하면서 업로드된 파일 아이디 dto에 넣기
            modifyRequestDto.getFileIds().add(fileId);
            if(modifyRequestDto.getFile2() != null){
                ResponseEntity<Object> response2 = fileService.uploadFile(modifyRequestDto.getFile2());
                String fileId2 = fileService.getFileId(response2);
                modifyRequestDto.getFileIds().add(fileId2);
            }
            //hasFile = true 설정
            modifyAssistantHasFileTrue(assistantId);
            if(modifyRequestDto.getModel().equals("gpt-4-1106-preview"))
                modifyRequestDto.setTools(Arrays.asList(new Tool("retrieval"), new Tool("code_interpreter")));
            else
                modifyRequestDto.setTools(List.of(new Tool("code_interpreter")));
        } else{ //새로 들어오는 파일이 없으면 -> 삭제된 거 있나 확인
            List<String> modifiedFileIds = modifyRequestDto.getFileIds();
            List<String> fileIds = openAiService.getFileIdList(openAiService.searchAssistant(assistantId));
            int fileIdsSize = fileIds.size();

            //삭제 되어야 하는 파일 아이디를 담는 배열
            List<String> toBeDeleted = new ArrayList<>();

            for (String fileId : fileIds) {
                if (!modifiedFileIds.contains(fileId)) {
                    toBeDeleted.add(fileId);
                }
            }
            for (String fileId : toBeDeleted) {
                fileService.deleteFile(fileId);
                fileIdsSize -= 1;
            }
            //붙은 file이 없으면 hasFile = false 설정
            if(fileIdsSize == 0){
                modifyAssistantHasFileFalse(assistantId);
            }
            else{
                if(modifyRequestDto.getModel().equals("gpt-4-1106-preview"))
                    modifyRequestDto.setTools(Arrays.asList(new Tool("retrieval"), new Tool("code_interpreter")));
                else
                    modifyRequestDto.setTools(List.of(new Tool("code_interpreter")));
            }
        }

        Assistant findOne = findById(assistantId);

        String setInstruction="";

        ResponseEntity<Object> res;
        if(modifyRequestDto.getModel().equals("gpt-4-1106-preview")){
            setInstruction = openAiService.setInstructionWithModel4(findOne.isHasFile(), modifyRequestDto.getInstruction(), nonNullFields);
            modifyRequestDto.setInstruction(setInstruction);
            res = openAiService.modifyAssistantWithModel4(assistantId, modifyRequestDto);

        }
        else{
            setInstruction = openAiService.setInstruction(modifyRequestDto.getInstruction(), nonNullFields);
            modifyRequestDto.setInstruction(setInstruction);
            res = openAiService.modifyAssistant(assistantId, modifyRequestDto);
        }


        try{
            //원본 객체 프로퍼티 get
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(modifyRequestDto);

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                if(name.equals("class")){
                    continue;
                }
                Object value = PropertyUtils.getProperty(modifyRequestDto, name);
                if(value == null){
                    BeanUtils.setProperty(findOne, name, null);
                } else{
                    BeanUtils.copyProperty(findOne, name, value);
                }
            }

        }catch (Exception e){
            throw new RuntimeException("Property copy with null handling failed",e);
        }
        return res;
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

    public OpenAiAssistantCreateRequestDto createAssistantWithModel3(AssistantCreateRequestDto assistantCreateRequestDto) {

        //enum 타입에 대해서만 필드가 null인지 아닌지 검사
        Map<String, Enum<?>> nonNullFields = getNonNullFieldsWhenCreate(assistantCreateRequestDto);
        OpenAiAssistantCreateRequestDto AiAssistantCreateDto = new OpenAiAssistantCreateRequestDto();
        boolean hasFile = false;


        //등록된 파일 있으면 먼저 서버에 저장
        if(assistantCreateRequestDto.getFile1() != null){
            hasFile = true;
            AiAssistantCreateDto.setTools(Arrays.asList(new Tool("retrieval"), new Tool ("code_interpreter")));
            ResponseEntity<Object> response = fileService.uploadFile(assistantCreateRequestDto.getFile1());
            String fileId = fileService.getFileId(response);
            AiAssistantCreateDto.getFileIds().add(fileId);

            if(assistantCreateRequestDto.getFile2()!=null){
                ResponseEntity<Object> response2 = fileService.uploadFile(assistantCreateRequestDto.getFile2());
                String fileId2 = fileService.getFileId(response2);
                AiAssistantCreateDto.getFileIds().add(fileId2);
            }
        }
        //튜터 성향 뽑아서 instruction에 넣기
        String setInstruction = openAiService.setInstruction(assistantCreateRequestDto.getInstruction(), nonNullFields);

        AiAssistantCreateDto.setName(assistantCreateRequestDto.getName());
        AiAssistantCreateDto.setInstruction(setInstruction);
        AiAssistantCreateDto.setDescription(assistantCreateRequestDto.getDescription());

        return AiAssistantCreateDto;
    }


    public OpenAiAssistantCreateRequestDto createAssistantWithModel4(AssistantCreateRequestDto assistantCreateRequestDto) {

        //enum 타입에 대해서만 필드가 null인지 아닌지 검사
        Map<String, Enum<?>> nonNullFields = getNonNullFieldsWhenCreate(assistantCreateRequestDto);
        OpenAiAssistantCreateRequestDto AiAssistantCreateDto = new OpenAiAssistantCreateRequestDto();
        boolean hasFile = false;

        //등록된 파일 있으면 먼저 서버에 저장
        if(assistantCreateRequestDto.getFile1() != null){
            hasFile = true;
            AiAssistantCreateDto.setTools(Arrays.asList(new Tool("retrieval"), new Tool ("code_interpreter")));
            ResponseEntity<Object> response = fileService.uploadFile(assistantCreateRequestDto.getFile1());
            String fileId = fileService.getFileId(response);
            AiAssistantCreateDto.getFileIds().add(fileId);

            if(assistantCreateRequestDto.getFile2()!=null){
                ResponseEntity<Object> response2 = fileService.uploadFile(assistantCreateRequestDto.getFile2());
                String fileId2 = fileService.getFileId(response2);
                AiAssistantCreateDto.getFileIds().add(fileId2);
            }
        }
        //튜터 성향 뽑아서 instruction에 넣기
        String setInstruction = openAiService.setInstructionWithModel4(hasFile, assistantCreateRequestDto.getInstruction(), nonNullFields);

        AiAssistantCreateDto.setName(assistantCreateRequestDto.getName());
        AiAssistantCreateDto.setInstruction(setInstruction);
        AiAssistantCreateDto.setDescription(assistantCreateRequestDto.getDescription());

        return AiAssistantCreateDto;
    }


    public void save(ResponseEntity<Object> assistantObject, AssistantCreateRequestDto assistantCreateRequestDto) {


        String assistantId = openAiService.getAssistantId(assistantObject);
        System.out.println("openAI에 저장된 어시스턴트 아이디!!!! = " + assistantId);

        //이미지 버킷에 저장하고 저장된 경로 반환
        String imgUrl = s3Service.uploadImage(assistantCreateRequestDto.getImgFile());

        boolean hasFile = assistantCreateRequestDto.getFile1() != null?true : false;



        //db에 어시스턴트 insert
        Assistant.AssistantBuilder builder =
                Assistant.builder().id(assistantId).name(assistantCreateRequestDto.getName()).img(imgUrl).description(assistantCreateRequestDto.getDescription())
                        .instruction(assistantCreateRequestDto.getInstruction()).hasFile(hasFile);

        //enum 타입에 대해서만 필드가 null인지 아닌지 검사
        Map<String, Enum<?>> nonNullFields = getNonNullFieldsWhenCreate(assistantCreateRequestDto);

        //enum 클래스에 한해서 값이 있는 필드만 세팅
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
        assistantRepository.save(builder.build());
    }
}
