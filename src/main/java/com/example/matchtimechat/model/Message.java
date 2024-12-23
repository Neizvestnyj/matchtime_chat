package com.example.matchtimechat.model;

import com.example.matchtimechat.serializer.ChatIdSerializer;
import com.example.matchtimechat.serializer.MessageIdSerializer;
import com.example.matchtimechat.serializer.UserIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на чат
    @ManyToOne(fetch = FetchType.LAZY) // Используем ленивую загрузку для оптимизации запросов
    @JoinColumn(name = "chat_id", nullable = false, foreignKey = @ForeignKey(name = "fk_messages_chat_id"))
    @JsonSerialize(using = ChatIdSerializer.class)
    private Chat chat;

    // Отправитель сообщения
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false, foreignKey = @ForeignKey(name = "fk_messages_sender_id"))
    @JsonSerialize(using = UserIdSerializer.class)
    private User sender;

    // Ответ на другое сообщение (опционально)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to", foreignKey = @ForeignKey(name = "fk_messages_reply_to"), nullable = true)
    @JsonSerialize(using = MessageIdSerializer.class)
    private Message replyTo;

    // Текст сообщения
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Прочитано или нет
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    // Время отправки сообщения
    @Column(name = "send_at", nullable = false, updatable = false)
    private LocalDateTime sendAt;

    // Устанавливаем дату отправки перед сохранением
    @PrePersist
    protected void onCreate() {
        this.sendAt = LocalDateTime.now();
    }
}
