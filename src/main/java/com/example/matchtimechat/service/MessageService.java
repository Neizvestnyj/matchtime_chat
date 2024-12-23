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
    private final ChatService chatService;

    public MessageService(MessageRepository messageRepository,
                          ChatRepository chatRepository,
                          UserRepository userRepository,
                          ChatService chatService) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatService = chatService;
    }

    public Message sendMessage(Long chatId, SendMessageDTO sendMessageDTO, String senderEmail) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        if (!chatService.isParticipant(chatId, senderEmail)) {
            throw new RuntimeException("You are not a participant of this chat");
        }

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
        message.setRead(false);

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(Long chatId, String userEmail) {
        if (!chatService.isParticipant(chatId, userEmail)) {
            throw new RuntimeException("Access denied");
        }
        return messageRepository.findByChatIdOrderBySendAtAsc(chatId);
    }

    public Message getMessageById(Long id, String userEmail) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!chatService.isParticipant(message.getChat().getId(), userEmail)) {
            throw new RuntimeException("Access denied");
        }

        return message;
    }

    public void deleteMessage(Long messageId, String userEmail) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not allowed to delete this message");
        }

        messageRepository.delete(message);
    }

    public boolean isMessageOwner(Long messageId, String userEmail) {
        Message message = messageRepository.findById(messageId)
                .orElse(null);
        return message != null && message.getSender().getEmail().equals(userEmail);
    }
}
