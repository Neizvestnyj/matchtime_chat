package com.example.matchtimechat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false, foreignKey = @ForeignKey(name = "fk_messages_chat_id"))
    @JsonIgnore // Полную сущность не сериализуем
    private Chat chat;

    @Transient // Добавляем для вывода только ID
    @JsonProperty("chatId")  // Устанавливаем имя поля в JSON как "chatId"
    public Long getChatId() {
        return chat != null ? chat.getId() : null;
    }

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false, foreignKey = @ForeignKey(name = "fk_messages_sender_id"))
    @JsonIgnore
    private User sender;

    @Transient
    @JsonProperty("senderId")  // Устанавливаем имя поля в JSON как "senderId"
    public Long getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    @ManyToOne
    @JoinColumn(name = "reply_to", foreignKey = @ForeignKey(name = "fk_messages_reply_to"), nullable = true)
    @JsonIgnore
    private Message replyTo;

    @Transient
    @JsonProperty("replyToId")  // Устанавливаем имя поля в JSON как "replyToId"
    public Long getReplyToId() {
        return replyTo != null ? replyTo.getId() : null;
    }

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "send_at", nullable = false, updatable = false)
    private LocalDateTime sendAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        this.sendAt = LocalDateTime.now();
    }
}
