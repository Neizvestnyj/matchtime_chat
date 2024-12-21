package com.example.matchtimechat.serializer;

import com.example.matchtimechat.model.Chat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ChatIdSerializer extends JsonSerializer<Chat> {
    @Override
    public void serialize(Chat chat, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(chat != null ? chat.getId() : null); // Выводим только `id`
    }
}

