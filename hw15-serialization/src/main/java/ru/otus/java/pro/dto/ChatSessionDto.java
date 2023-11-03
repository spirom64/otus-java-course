package ru.otus.java.pro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatSessionDto {
    @JsonProperty("chat_id")
    private long chatId;

    public String getChatIdentifier() {
        return chatIdentifier;
    }

    @JsonProperty("chat_identifier")
    private String chatIdentifier;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("is_deleted")
    private int isDeleted;

    @JsonProperty("members")
    private List<MemberDto> members;

    public List<MessageDto> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    private List<MessageDto> messages;

    public String getMemberLastByHandleId(int handleId) {
        for (MemberDto member : members) {
            if (member.getHandleId() == handleId) {
                return member.getLast();
            }
        }
        return null;
    }
}
