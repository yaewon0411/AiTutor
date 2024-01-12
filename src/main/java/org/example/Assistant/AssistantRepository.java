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
    @Query("update Assistant a set a.personality = :personality, a.speechLevel = :speechLevel, a.voice = :voice where a.id = :id")
    void updateOptions(@Param("id")String id, @Param("personality")Personality personality, @Param("speechLevel")SpeechLevel speechLevel, @Param("voice")Voice voice);

    @Modifying
    @Query("update Assistant a set a.img = :img where a.id = :id")
    void updateAssistantImgById(@Param("id")String assistantId, @Param("img")String img);

    @Modifying
    @Query("delete from Assistant a where a.id = :id")
    void deleteAssistantById(@Param("id")String assistantId);
}
