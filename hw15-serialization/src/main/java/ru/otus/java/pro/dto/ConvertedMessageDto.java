package ru.otus.java.pro.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ConvertedMessageDto {
    public Instant getSendDate() {
        return sendDate;
    }

    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss", timezone = "Europe/Moscow")
    @JsonProperty("chat_sessions.messages.send_date")
    private final Instant sendDate;

    @JsonProperty("chat_sessions.chat_identifier")
    private final String chatIdentifier;

    @JsonProperty("chat_sessions.members.last")
    private final String membersLast;

    @JsonProperty("chat_sessions.messages.text")
    private final String text;

    @JsonCreator
    public ConvertedMessageDto(
            @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss", timezone = "Europe/Moscow") @JsonProperty("chat_sessions.messages.send_date") Instant sendDate,
            @JsonProperty("chat_sessions.chat_identifier") String chatIdentifier,
            @JsonProperty("chat_sessions.members.last") String membersLast,
            @JsonProperty("chat_sessions.messages.text") String text) {
        this.sendDate = sendDate;
        this.chatIdentifier = chatIdentifier;
        this.membersLast = membersLast;
        this.text = text;
    }
}
