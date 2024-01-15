package org.example.Assistant;

import lombok.RequiredArgsConstructor;
import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;
import org.example.Assistant.dto.ShowHomeDto;
import org.example.Assistant.dto.TutorInfoDto;
import org.example.Assistant.dto.TutorModifyDto;
import org.example.Assistant.dto.TutoringPageDto;
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
        return new TutorModifyDto(findOne.getName(), findOne.getImg(), findOne.getDescription(), findOne.getPersonality(),findOne.getSpeechLevel(),findOne.getVoice(), findOne.getInstruction());
    }
    @Transactional(readOnly = true)
    public String getAssistantVoice(String assistantId){
        Assistant findOne = assistantRepository.findById(assistantId).get();
        Voice voice = findOne.getVoice();
        if(voice.toString().equals("Female")) return "nova";
        else return "alloy";
    }


    public void modifyAssistantPersonality(Personality personality, String assistantId){
        assistantRepository.updateAssistantPersonalityById(personality, assistantId);
    }



    public void modifyAssistantImg(String assistantId, String img) {
        assistantRepository.updateAssistantImgById(assistantId, img);
    }


    public void deleteAssistant(String assistantId) {
        assistantRepository.deleteAssistantById(assistantId);
    }


    public void setHasFileTrue(Assistant findOne) {
        findOne.setHasFileTure();
    }


    public void modifyAssistantName(String name, String assistantId) {
        assistantRepository.updateAssistantNameById(name, assistantId);
    }


    public void modifyAssistantDescription(String description, String assistantId) {
        assistantRepository.updateAssistantDescriptionById(description, assistantId);
    }


    public void modifyAssistantInstruction(String instructions, String assistantId) {
        assistantRepository.updateAssistantInstructionById(instructions, assistantId);
    }

    public void modifyAssistantSpeechLevel(SpeechLevel speechLevel, String assistantId) {
        assistantRepository.updateAssistantSpeechLevelById(speechLevel, assistantId);
    }

    public void modifyAssistantHasFile(String assistantId) {
        assistantRepository.updateAssistantHasFileById(assistantId);
    }

    public void modifyAssistantVoice(Voice voice, String assistantId) {
        assistantRepository.updateAssistantVoiceById(voice, assistantId);
    }
}
