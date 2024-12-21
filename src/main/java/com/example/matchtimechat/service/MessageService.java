package com.example.matchtimechat.service;

import com.example.matchtimechat.dto.SendMessageDTO;
import com.example.matchtimechat.model.Chat;
import com.example.matchtimechat.model.Message;
import com.example.matchtimechat.model.User;
import com.example.matchtimechat.repository.ChatRepository;
import com.example.matchtimechat.repository.MessageRepository;
import com.example.matchtimechat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public Message sendMessage(SendMessageDTO sendMessageDTO) {
        Chat chat = chatRepository.findById(sendMessageDTO.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User sender = userRepository.findById(sendMessageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message replyTo = null;
        if (sendMessageDTO.getReplyTo() != null) {
            replyTo = messageRepository.findById(sendMessageDTO.getReplyTo())
                    .orElse(null); // Reply-to is optional
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(sendMessageDTO.getContent());
        message.setReplyTo(replyTo);
        message.setRead(false); // Устанавливаем статус "не прочитано"

        return messageRepository.save(message);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatIdOrderBySendAtAsc(chatId);
    }

    public void deleteMessageById(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
    }
}
