package org.example.Assistant;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.example.Assistant.Enum.Voice;
import org.example.Assistant.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RealService {
    private final AssistantRepository assistantRepository;
    private final ModelMapper modelMapper;

    public void save(Assistant assistant) {
        assistantRepository.save(assistant);
    }
    @Transactional(readOnly = true)
    public List<ShowHomeDto> findAll(){
        List<Assistant> all = assistantRepository.findAll();
        List<ShowHomeDto> res = new ArrayList<>();
        for (Assistant assistant : all) {
            ShowHomeDto dto = new ShowHomeDto(assistant.getName(), assistant.getImg(), assistant.getId());
            res.add(dto);
        }
        return res;
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
        Assistant findOne = findById(assistantId);
        TutorModifyDto res = modelMapper.map(findOne, TutorModifyDto.class);
        return res;
    }
    @Transactional(readOnly = true)
    public String getAssistantVoice(String assistantId){
        Assistant findOne = findById(assistantId);
        Voice voice = findOne.getVoice();
        if(voice.toString().equals("Female")) return "shimmer";
        else return "onyx";
    }

    public void modifyAssistantImg(String assistantId, String img) {
        assistantRepository.updateAssistantImgById(assistantId, img);
    }

    public void deleteAssistant(String assistantId) {
        assistantRepository.deleteAssistantById(assistantId);
    }


    public void modifyAssistantHasFileTrue(String assistantId) {
        Assistant findOne = findById(assistantId);
        findOne.setHasFileTure();
    }

    public void modifyAssistantHasFileFalse(String assistantId){
        Assistant findOne = findById(assistantId);
        findOne.setHasFileFalse();
    }

    public List<ShowHomeDto> searchByKeyword(String keyword){
        List<Assistant> assistants = assistantRepository.searchByKeyword(keyword);
        List<ShowHomeDto> showHomeDtoList = new ArrayList<>();
        for (Assistant assistant : assistants) {
            ShowHomeDto searchDto = new ShowHomeDto(assistant.getName(), assistant.getImg(),assistant.getId());
            showHomeDtoList.add(searchDto);
        }
        return showHomeDtoList;
    }


    public void updateAssistant(String assistantId, ModifyRequestDto modifyRequestDto) throws IllegalAccessException {

        Assistant findOne = findById(assistantId);
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
        //변경 사항 적용
        assistantRepository.save(findOne);
    }
}
