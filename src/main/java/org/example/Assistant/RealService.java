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
@Transactional(readOnly = true)
public class RealService {
    private final AssistantRepository assistantRepository;
    @Transactional
    public void save(Assistant assistant) {
        assistantRepository.save(assistant);
    }
    public List<ShowHomeDto> findAll(){
        List<Assistant> all = assistantRepository.findAll();
        List<ShowHomeDto> res = new ArrayList<>();
        for (Assistant assistant : all) {
            ShowHomeDto dto = new ShowHomeDto(assistant.getName(), assistant.getImg());
            res.add(dto);
        }
        return res;
    }
    public Assistant findById(String assistantId){
        return assistantRepository.findById(assistantId).get();
    }

    public TutoringPageDto findByIdInTutoringPage(String assistantId) {
        Assistant findOne = assistantRepository.findById(assistantId).get();
        return new TutoringPageDto(findOne.getName(), findOne.getDescription());
    }

    public TutorInfoDto getTutorInfo(String assistantId){
        Assistant findOne = assistantRepository.findById(assistantId).get();
        return new TutorInfoDto(findOne.getName(), findOne.getImg(), findOne.getDescription(), findOne.getPersonality(),findOne.getSpeechLevel(),findOne.getVoice());
    }

    //튜터 수정 화면에 뿌려지는 정보
    public TutorModifyDto getTutorInfoToModify(String assistantId){
        Assistant findOne = assistantRepository.findById(assistantId).get();
        return new TutorModifyDto(findOne.getName(), findOne.getImg(), findOne.getDescription(), findOne.getPersonality(),findOne.getSpeechLevel(),findOne.getVoice(), findOne.getInstruction());
    }
    public String getAssistantVoice(String assistantId){
        Assistant findOne = assistantRepository.findById(assistantId).get();
        Voice voice = findOne.getVoice();
        if(voice.toString().equals("Female")) return "nova";
        else return "alloy";
    }

    public void modify(String assistantId, String personality, String speechLevel, String voice) {

        Personality p = Personality.valueOf(personality);
        SpeechLevel s = SpeechLevel.valueOf(speechLevel);
        Voice v = Voice.valueOf(voice);

        assistantRepository.updateOptions(assistantId, p, s, v);
    }


    @Transactional
    public void modifyAssistantImg(String assistantId, String img) {
        assistantRepository.updateAssistantImgById(assistantId, img);
    }

    @Transactional
    public void deleteAssistant(String assistantId) {
        assistantRepository.deleteAssistantById(assistantId);
    }

    @Transactional
    public void setHasFileTrue(Assistant findOne) {
        findOne.setHasFileTure();
    }
}
