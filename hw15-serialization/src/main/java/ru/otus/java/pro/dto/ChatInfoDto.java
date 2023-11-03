package ru.otus.java.pro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.Timestamp;
import ru.otus.java.pro.protobuf.ConvertedMessagesProtos;

import java.time.Instant;
import java.util.*;

public class ChatInfoDto {
    @JsonProperty("chat_sessions")
    private List<ChatSessionDto> chatSessions;

    private Timestamp convertInstantToTimestamp(Instant date) {
        return Timestamp.newBuilder().setSeconds(date.getEpochSecond()).setNanos(date.getNano()).build();
    }

    private Instant convertTimestampToInstant(Timestamp date) {
        return Instant.ofEpochSecond(date.getSeconds(), date.getNanos());
    }

    public ConvertedMessagesDto convertToJacksonSerializable() {
        Map<String, MessagesByNumberDto> messagesByNumberMap = new HashMap<>();
        ConvertedMessagesDto result = new ConvertedMessagesDto();

        for (ChatSessionDto chatSession : chatSessions) {
            for (MessageDto message : chatSession.getMessages()) {
                MessagesByNumberDto messagesByNumberDto = messagesByNumberMap.computeIfAbsent(message.getBelongNumber(), MessagesByNumberDto::new);
                messagesByNumberDto.addMessage(new ConvertedMessageDto(
                        message.getSendDate(),
                        chatSession.getChatIdentifier(),
                        chatSession.getMemberLastByHandleId(message.getHandleId()),
                        message.getText()));
            }
        }

        for (Map.Entry<String, MessagesByNumberDto> entry : messagesByNumberMap.entrySet()) {
            entry.getValue().sortMessages();
            result.addMessages(entry.getValue());
        }

        return result;
    }

    public ConvertedMessagesProtos.ConvertedMessages convertToProtobufSerializable() {
        Map<String, ConvertedMessagesProtos.MessagesByPhoneNumber.Builder> messagesByNumberMap = new HashMap<>();
        ConvertedMessagesProtos.ConvertedMessages.Builder result = ConvertedMessagesProtos.ConvertedMessages.newBuilder();

        for (ChatSessionDto chatSession : chatSessions) {
            for (MessageDto message : chatSession.getMessages()) {
                ConvertedMessagesProtos.MessagesByPhoneNumber.Builder messagesByPhoneNumber = messagesByNumberMap.computeIfAbsent(
                        message.getBelongNumber(),
                        number -> ConvertedMessagesProtos.MessagesByPhoneNumber.newBuilder().setBelongNumber(number)
                );
                messagesByPhoneNumber.addMessages(ConvertedMessagesProtos.ConvertedMessage
                        .newBuilder()
                        .setChatIdentifier(chatSession.getChatIdentifier())
                        .setMembersLast(chatSession.getMemberLastByHandleId(message.getHandleId()))
                        .setMessageText(message.getText())
                        .setSendDate(convertInstantToTimestamp(message.getSendDate()))
                        .build()
                );
            }
        }

        for (Map.Entry<String, ConvertedMessagesProtos.MessagesByPhoneNumber.Builder> entry : messagesByNumberMap.entrySet()) {
            List<ConvertedMessagesProtos.ConvertedMessage> messages = new ArrayList<>(entry.getValue().getMessagesList());
            messages.sort(Comparator.comparing(m -> convertTimestampToInstant(m.getSendDate())));
            result.addMessages(entry.getValue().clearMessages().addAllMessages(messages).build());
        }

        return result.build();
    }
}
