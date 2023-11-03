package ru.otus.java.pro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.otus.java.pro.dto.ChatInfoDto;
import ru.otus.java.pro.dto.ConvertedMessagesDto;
import ru.otus.java.pro.protobuf.ConvertedMessagesProtos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class MessagesSerializer {
    private static final Logger log = Logger.getLogger(MessagesSerializer.class.getName());

    private static ObjectMapper getObjectMapper(String format) {
        ObjectMapper mapper;

        switch (format.toLowerCase()) {
            case "xml":
                mapper = new XmlMapper();
                break;
            case "yaml":
                mapper = new ObjectMapper(new YAMLFactory());
                break;
            default:
                mapper = new ObjectMapper();
        }

        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private static Object deserializeFromFile(String fileName, String format, Class<?> schema) {
        ObjectMapper mapper = getObjectMapper(format);

        try {
            return mapper.readValue(new File(fileName), schema);
        } catch (Exception e) {
            log.warning(e.getMessage());
            return null;
        }
    }

    private static void serializeToFile(String fileName, String format, Object value) {
        ObjectMapper mapper = getObjectMapper(format);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), value);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    public static String writeAsString(String format, Object value) {
        ObjectMapper mapper = getObjectMapper(format);

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            log.warning(e.getMessage());
            return null;
        }
    }

    public static ChatInfoDto readChatInfoFromFile(String fileName, String format) {
        return (ChatInfoDto) deserializeFromFile(fileName, format, ChatInfoDto.class);
    }

    public static ConvertedMessagesDto readConvertedMessagesFromFile(String fileName, String format) {
        return (ConvertedMessagesDto) deserializeFromFile(fileName, format, ConvertedMessagesDto.class);
    }

    public static void writeConvertedMessagesToFile(String fileName, String format, ConvertedMessagesDto messages) {
        serializeToFile(fileName, format, messages);
    }

    public static void writeConvertedMessagesToFile(String fileName, ConvertedMessagesProtos.ConvertedMessages messages) {
        try {
            messages.writeTo(new FileOutputStream(fileName));
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    public static ConvertedMessagesProtos.ConvertedMessages readConvertedMessagesFromFile(String fileName) {
        try {
            return ConvertedMessagesProtos.ConvertedMessages.newBuilder().mergeFrom(new FileInputStream(fileName)).build();
        } catch (Exception e) {
            log.warning(e.getMessage());
            return null;
        }
    }
}
