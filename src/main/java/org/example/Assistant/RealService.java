package org.example.Assistant;

import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;
import org.example.Assistant.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return assistantRepository.findById(assistantId).get();
    }

    @Transactional(readOnly = true)
    public TutoringPageDto findByIdInTutoringPage(String assistantId) {
        Assistant findOne = assistantRepository.findById(assistantId).get();
        return new TutoringPageDto(findOne.getName(), findOne.getDescription(), findOne.getImg());
    }

    @Transactional(readOnly = true)
    public TutorInfoDto getTutorInfo(String assistantId){
        Assistant findOne = assistantRepository.findById(assistantId).get();
        return new TutorInfoDto(findOne.getName(), findOne.getImg(), findOne.getDescription(), findOne.getPersonality(),findOne.getSpeechLevel(),findOne.getVoice());
    }

    //튜터 수정 화면에 뿌려지는 정보
    @Transactional(readOnly = true)
    public TutorModifyDto getTutorInfoToModify(String assistantId){
        Assistant findOne = assistantRepository.findById(assistantId).get();
        TutorModifyDto res = modelMapper.map(findOne, TutorModifyDto.class);
        return res;
    }
    @Transactional(readOnly = true)
    public String getAssistantVoice(String assistantId){
        Assistant findOne = assistantRepository.findById(assistantId).get();
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
        assistantRepository.updateAssistantHasFileTrueById(assistantId);
    }
    public void modifyAssistantHasFileFalse(String assistantId){
        assistantRepository.updateAssistantHasFileFalseById(assistantId);
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

    @Transactional
    public void updateAssistant(String assistantId, ModifyRequestDto modifyRequestDto) throws IllegalAccessException {
        Assistant findOne = assistantRepository.findById(assistantId).get();
        if(findOne == null) throw new IllegalAccessException("해당 튜터를 찾을 수 없습니다.");

        BeanUtils.copyProperties(modifyRequestDto, findOne, "id");

        if(modifyRequestDto.getPersonality() == null) findOne.setPersonality(null);
        if(modifyRequestDto.getSpeechLevel() == null) findOne.setSpeechLevel(null);
        if(modifyRequestDto.getAnswerDetail() == null) findOne.setAnswerDetail(null);
        if(modifyRequestDto.getConversationalStyle() == null) findOne.setConversationalStyle(null);
        if(modifyRequestDto.getEmoji() == null) findOne.setEmoji(null);
        if(modifyRequestDto.getEmotionalExpression() == null) findOne.setEmotionalExpression(null);
        if(modifyRequestDto.getLanguageMode() == null) findOne.setLanguageMode(null);
        if(modifyRequestDto.getUseOfTechnicalLanguage() == null) findOne.setUseOfTechnicalLanguage(null);

        //변경 사항 적용
        assistantRepository.save(findOne);
    }
}
