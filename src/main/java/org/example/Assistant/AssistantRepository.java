package org.example.Assistant;

import org.example.Assistant.Enum.Personality;
import org.example.Assistant.Enum.SpeechLevel;
import org.example.Assistant.Enum.Voice;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, String> {

    List<Assistant> findAll();

    @Override
    Optional<Assistant> findById(String assistantId);


    @Modifying
    @Query("update Assistant a set a.img = :img where a.id = :id")
    void updateAssistantImgById(@Param("id")String assistantId, @Param("img")String img);

    @Modifying
    @Query("delete from Assistant a where a.id = :id")
    void deleteAssistantById(@Param("id")String assistantId);

    @Modifying
    @Query("update Assistant a set a.description = :d where a.id = :id")
    void updateAssistantDescriptionById(@Param("d")String description, @Param("id") String assistantId);

    @Modifying
    @Query("update Assistant a set a.instruction = :i where a.id = :id")
    void updateAssistantInstructionById(@Param("i")String instructions, @Param("id") String assistantId);

    @Modifying
    @Query("update Assistant a set a.name = :name where a.id = :id")
    void updateAssistantNameById(@Param("name")String name, @Param("id")String assistantId);

    @Modifying
    @Query("update Assistant a set a.personality = :p where a.id = :id")
    void updateAssistantPersonalityById(@Param("p") Personality personality,@Param("id") String assistantId);

    @Modifying
    @Query("update Assistant a set a.speechLevel = :s where a.id = :id")
    void updateAssistantSpeechLevelById(@Param("s") SpeechLevel speechLevel,@Param("id") String assistantId);

    @Modifying
    @Query("update Assistant a set a.hasFile = true where a.id = :id")
    void updateAssistantHasFileById(@Param("id") String assistantId);

    @Modifying
    @Query("update Assistant a set a.voice = :v where a.id = :id")
    void updateAssistantVoiceById(@Param("v") Voice voice,@Param("id") String assistantId);

    @Query("select a from Assistant a where a.name like %:keyword% or a.description like %:keyword%")
    List<Assistant> searchByKeyword(@Param("keyword")String keyword);

}
