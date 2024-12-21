package com.example.matchtimechat.controller;

import com.example.matchtimechat.dto.AddParticipantDTO;
import com.example.matchtimechat.dto.CreateChatDTO;
import com.example.matchtimechat.model.Chat;
import com.example.matchtimechat.model.ChatParticipant;
import com.example.matchtimechat.model.User;
import com.example.matchtimechat.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Chat createChat(@RequestBody CreateChatDTO createChatDTO) {
        return chatService.createChat(createChatDTO);
    }

    @PostMapping("/{chatId}/participants")
    public ChatParticipant addParticipant(@PathVariable Long chatId, @RequestBody AddParticipantDTO addParticipantDTO) {
        return chatService.addParticipant(chatId, addParticipantDTO);
    }

    // Метод для получения всех участников чата
    @GetMapping("/{chatId}/participants")
    public List<User> getChatParticipants(@PathVariable Long chatId) {
        return chatService.getAllParticipants(chatId);
    }
}

