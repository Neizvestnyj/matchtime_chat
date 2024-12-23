package com.example.matchtimechat.repository;

import com.example.matchtimechat.model.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByChatId(Long chatId);
    boolean existsByChatIdAndUserId(Long chatId, Long userId);
}
