package com.example.matchtimechat.model;

import com.example.matchtimechat.serializer.ChatIdSerializer;
import com.example.matchtimechat.serializer.UserIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_participants")
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonSerialize(using = ChatIdSerializer.class)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonSerialize(using = UserIdSerializer.class)
    private User user;

    private LocalDateTime joinedAt;

    @PrePersist
    protected void onJoin() {
        this.joinedAt = LocalDateTime.now();
    }
}
