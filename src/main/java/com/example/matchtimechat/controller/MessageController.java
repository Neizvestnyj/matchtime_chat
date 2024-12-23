package com.example.matchtimechat.controller;

import com.example.matchtimechat.dto.SendMessageDTO;
import com.example.matchtimechat.model.Message;
import com.example.matchtimechat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageDTO sendMessageDTO, Principal principal) {
        Message message = messageService.sendMessage(sendMessageDTO.getChatId(), sendMessageDTO, principal.getName());
        return ResponseEntity.status(201).body(message);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChat(@PathVariable Long chatId, Principal principal) {
        List<Message> messages = messageService.getMessagesByChatId(chatId, principal.getName());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id, Principal principal) {
        Message message = messageService.getMessageById(id, principal.getName());
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long id, Principal principal) {
        messageService.deleteMessage(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
