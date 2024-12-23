package com.example.matchtimechat.service;

import com.example.matchtimechat.dto.AddParticipantDTO;
import com.example.matchtimechat.dto.CreateChatDTO;
import com.example.matchtimechat.model.Chat;
import com.example.matchtimechat.model.ChatParticipant;
import com.example.matchtimechat.model.User;
import com.example.matchtimechat.repository.ChatParticipantRepository;
import com.example.matchtimechat.repository.ChatRepository;
import com.example.matchtimechat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, ChatParticipantRepository chatParticipantRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.userRepository = userRepository;
    }

    public Chat createChat(CreateChatDTO createChatDTO, String creatorEmail) {
        User owner = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Chat chat = new Chat();
        chat.setName(createChatDTO.getName());
        chat.setOwner(owner);
        Chat savedChat = chatRepository.save(chat);

        ChatParticipant participant = new ChatParticipant();
        participant.setChat(savedChat);
        participant.setUser(owner);
        chatParticipantRepository.save(participant);

        return savedChat;
    }

    public ChatParticipant addParticipant(Long chatId, AddParticipantDTO addParticipantDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User user = userRepository.findById(addParticipantDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUser(user);

        return chatParticipantRepository.save(participant);
    }

    public List<User> getAllParticipants(Long chatId) {
        return chatParticipantRepository.findByChatId(chatId).stream()
                .map(ChatParticipant::getUser)
                .collect(Collectors.toList());
    }

    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public boolean isParticipant(Long chatId, String userEmail) {
        return chatParticipantRepository.findByChatId(chatId).stream()
                .anyMatch(participant -> participant.getUser().getEmail().equals(userEmail));
    }

}
