package com.example.matchtimechat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageDTO {
    private Long chatId;
    private Long senderId;
    private String content;
    private Long replyTo; // Optional
}
