package com.example.matchtimechat.controller;

import com.example.matchtimechat.dto.SendMessageDTO;
import com.example.matchtimechat.model.Message;
import com.example.matchtimechat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Message sendMessage(@RequestBody SendMessageDTO sendMessageDTO) {
        return messageService.sendMessage(sendMessageDTO);
    }

    @GetMapping("/chat/{chatId}")
    public List<Message> getMessagesByChat(@PathVariable Long chatId) {
        return messageService.getMessagesByChatId(chatId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long id) {
        messageService.deleteMessageById(id);
        return ResponseEntity.noContent().build();
    }
}
