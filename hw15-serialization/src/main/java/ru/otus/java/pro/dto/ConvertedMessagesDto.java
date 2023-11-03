package ru.otus.java.pro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ConvertedMessagesDto {
    @JsonProperty("messages")
    private final List<MessagesByNumberDto> messages = new ArrayList<>();

    public void addMessages(MessagesByNumberDto messages) {
        this.messages.add(messages);
    }
}
