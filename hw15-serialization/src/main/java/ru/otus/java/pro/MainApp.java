package ru.otus.java.pro;

import ru.otus.java.pro.dto.ChatInfoDto;
import ru.otus.java.pro.dto.ConvertedMessagesDto;
import ru.otus.java.pro.protobuf.ConvertedMessagesProtos;


public class MainApp {
    public static void main(String... args) {
        ChatInfoDto chatInfo = MessagesSerializer.readChatInfoFromFile("hw15-serialization/sms.json", "json");
        ConvertedMessagesDto messages = chatInfo.convertToJacksonSerializable();

        System.out.println("\nJSON\n\n");

        MessagesSerializer.writeConvertedMessagesToFile("converted.json", "json", messages);
        ConvertedMessagesDto messagesDeserialized = MessagesSerializer.readConvertedMessagesFromFile("converted.json", "json");
        System.out.println(MessagesSerializer.writeAsString("json", messagesDeserialized));

        System.out.println("\n\nXML\n\n");

        MessagesSerializer.writeConvertedMessagesToFile("converted.xml", "xml", messages);
        messagesDeserialized = MessagesSerializer.readConvertedMessagesFromFile("converted.xml", "xml");
        System.out.println(MessagesSerializer.writeAsString("xml", messagesDeserialized));

        System.out.println("\n\nYAML\n\n");

        MessagesSerializer.writeConvertedMessagesToFile("converted.yml", "yaml", messages);
        messagesDeserialized = MessagesSerializer.readConvertedMessagesFromFile("converted.yml", "yaml");
        System.out.println(MessagesSerializer.writeAsString("yaml", messagesDeserialized));

        System.out.println("\n\nProtobuf\n\n");

        ConvertedMessagesProtos.ConvertedMessages messagesProto = chatInfo.convertToProtobufSerializable();
        MessagesSerializer.writeConvertedMessagesToFile("converted.proto", messagesProto);
        ConvertedMessagesProtos.ConvertedMessages messagesProtoDeserialized = MessagesSerializer.readConvertedMessagesFromFile("converted.proto");
        System.out.println(messagesProtoDeserialized);
    }
}
