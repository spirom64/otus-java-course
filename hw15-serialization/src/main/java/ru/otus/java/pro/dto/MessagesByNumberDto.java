package ru.otus.java.pro.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessagesByNumberDto {
    @JsonProperty("chat_sessions.messages.belong_number")
    private final String belongNumber;

    @JsonProperty("messages")
    private List<ConvertedMessageDto> messages = new ArrayList<>();

    public MessagesByNumberDto(String belongNumber) {
        this.belongNumber = belongNumber;
    }

    @JsonCreator
    public MessagesByNumberDto(
            @JsonProperty("chat_sessions.messages.belong_number") String belongNumber,
            @JsonProperty("messages") List<ConvertedMessageDto> messages) {
        this.belongNumber = belongNumber;
        this.messages = messages;
    }

    public void addMessage(ConvertedMessageDto message) {
        messages.add(message);
    }

    public void sortMessages() {
        messages.sort(Comparator.comparing(ConvertedMessageDto::getSendDate));
    }
}
