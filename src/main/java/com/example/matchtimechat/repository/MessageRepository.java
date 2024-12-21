package com.example.matchtimechat.repository;

import com.example.matchtimechat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderBySendAtAsc(Long chat);
}
