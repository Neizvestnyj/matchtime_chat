package com.example.matchtimechat.controller;

import com.example.matchtimechat.dto.AddParticipantDTO;
import com.example.matchtimechat.dto.CreateChatDTO;
import com.example.matchtimechat.dto.SendMessageDTO;
import com.example.matchtimechat.model.Chat;
import com.example.matchtimechat.model.ChatParticipant;
import com.example.matchtimechat.model.Message;
import com.example.matchtimechat.model.User;
import com.example.matchtimechat.service.ChatService;
import com.example.matchtimechat.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    public ChatController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody CreateChatDTO createChatDTO, Principal principal) {
        Chat chat = chatService.createChat(createChatDTO, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }

    @PostMapping("/{chatId}/participants")
    public ResponseEntity<?> addParticipant(@PathVariable Long chatId, @RequestBody AddParticipantDTO addParticipantDTO, Principal principal) {
        if (!chatService.isParticipant(chatId, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        ChatParticipant participant = chatService.addParticipant(chatId, addParticipantDTO);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/{chatId}/participants")
    public ResponseEntity<List<User>> getParticipants(@PathVariable Long chatId, Principal principal) {
        if (!chatService.isParticipant(chatId, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<User> participants = chatService.getAllParticipants(chatId);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Message> createMessage(@PathVariable Long chatId, @RequestBody SendMessageDTO sendMessageDTO, Principal principal) {
        if (!chatService.isParticipant(chatId, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Message message = messageService.sendMessage(chatId, sendMessageDTO, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long chatId, Principal principal) {
        if (!chatService.isParticipant(chatId, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<Message> messages = messageService.getMessagesByChatId(chatId, principal.getName());
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId, Principal principal) {
        if (!messageService.isMessageOwner(messageId, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        messageService.deleteMessage(messageId, principal.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable Long chatId, Principal principal) {
        chatService.deleteChat(chatId, principal.getName());
        return ResponseEntity.ok().build();
    }
}
