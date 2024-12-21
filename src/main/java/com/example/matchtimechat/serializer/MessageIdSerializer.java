package com.example.matchtimechat.serializer;

import com.example.matchtimechat.model.Message;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MessageIdSerializer extends JsonSerializer<Message> {
    @Override
    public void serialize(Message message, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(message != null ? message.getId() : null); // Выводим только `id` сообщения
    }
}
