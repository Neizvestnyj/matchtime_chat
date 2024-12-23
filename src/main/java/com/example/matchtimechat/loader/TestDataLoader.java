package com.example.matchtimechat.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import com.example.matchtimechat.model.*;
import com.example.matchtimechat.repository.ChatRepository;
import com.example.matchtimechat.repository.MessageRepository;
import com.example.matchtimechat.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final Environment environment;
    private static final Logger logger = LoggerFactory.getLogger(TestDataLoader.class);

    public TestDataLoader(UserRepository userRepository, ChatRepository chatRepository, MessageRepository messageRepository, Environment environment) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        logger.info("Dev: {}", List.of(environment.getActiveProfiles()).contains("dev"));

        if (!List.of(environment.getActiveProfiles()).contains("dev")) {
            return; // Не загружаем данные, если профиль не dev
        }

        // Проверка, есть ли данные
        if (userRepository.count() > 0 || chatRepository.count() > 0 || messageRepository.count() > 0) {
            logger.info("Данные уже загружены, пропускаем загрузку.");
            return;
        }

        logger.info("Создание тестовых данных...");

        // === Создание пользователей ===
        User user1 = new User();
        user1.setEmail("alice@example.com");
        user1.setFirstName("Alice");
        user1.setLastName("Johnson");
        user1.setPasswordHash("hashed_password_1");
        user1.setRole("USER");
        user1.setActive(true);

        User user2 = new User();
        user2.setEmail("bob@example.com");
        user2.setFirstName("Bob");
        user2.setLastName("Smith");
        user2.setPasswordHash("hashed_password_2");
        user2.setRole("USER");
        user2.setActive(true);

        User user3 = new User();
        user3.setEmail("admin@example.com");
        user3.setFirstName("Admin");
        user3.setLastName("Adminson");
        user3.setPasswordHash("hashed_password_admin");
        user3.setRole("ADMIN");
        user3.setActive(true);

        userRepository.saveAll(List.of(user1, user2, user3));

        // === Создание чатов ===
        Chat chat1 = new Chat();
        chat1.setOwner(user1);
        chat1.setName("General Chat");

        Chat chat2 = new Chat();
        chat2.setOwner(user2);
        chat2.setName("Project Discussion");

        chatRepository.saveAll(List.of(chat1, chat2));

        // === Создание сообщений ===
        Message message1 = new Message();
        message1.setChat(chat1);
        message1.setSender(user1);
        message1.setContent("Hello everyone!");
        message1.setRead(true);

        Message message2 = new Message();
        message2.setChat(chat1);
        message2.setSender(user2);
        message2.setContent("Hi Alice!");
        message2.setRead(false);

        Message message3 = new Message();
        message3.setChat(chat2);
        message3.setSender(user3);
        message3.setContent("Let's discuss the project timeline.");
        message3.setRead(true);

        messageRepository.saveAll(List.of(message1, message2, message3));
    }
}
