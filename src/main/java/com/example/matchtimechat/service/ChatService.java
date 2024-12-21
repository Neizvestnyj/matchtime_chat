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

import java.util.ArrayList;
import java.util.List;

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

    // Метод для получения всех участников чата
    public List<User> getAllParticipants(Long chatId) {
        // Получаем список участников чата через ChatParticipantRepository
        List<ChatParticipant> participants = chatParticipantRepository.findByChatId(chatId);

        // Извлекаем пользователей из ChatParticipant и возвращаем их
        List<User> users = new ArrayList<>();
        for (ChatParticipant participant : participants) {
            users.add(participant.getUser());
        }

        return users;
    }

    public Chat createChat(CreateChatDTO createChatDTO) {
        User user = userRepository.findById(createChatDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Создаем новый чат
        Chat chat = new Chat();
        chat.setCreatedBy(user);
        chat.setName(createChatDTO.getName());
        chat = chatRepository.save(chat);

        // Автоматически добавляем создателя как участника чата
        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUser(user);
        chatParticipantRepository.save(participant);

        return chat;
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
}
