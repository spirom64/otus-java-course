package ru.otus.java.pro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class MessageDto {
    @JsonProperty("ROWID")
    private long rowId;

    @JsonProperty("attributedBody")
    private String attributedBody;

    @JsonProperty("belong_number")
    private String belongNumber;

    @JsonProperty("date")
    private long date;

    @JsonProperty("date_read")
    private long dateRead;

    @JsonProperty("guid")
    private String guid;

    @JsonProperty("handle_id")
    private int handleId;

    @JsonProperty("has_dd_results")
    private int hasDdResults;

    @JsonProperty("is_deleted")
    private int isDeleted;

    @JsonProperty("is_from_me")
    private int isFromMe;

    public String getBelongNumber() {
        return belongNumber;
    }

    public int getHandleId() {
        return handleId;
    }

    public Instant getSendDate() {
        return sendDate;
    }

    public String getText() {
        return text;
    }

    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss", timezone = "Europe/Moscow")
    @JsonProperty("send_date")
    private Instant sendDate;

    @JsonProperty("send_status")
    private int sendStatus;

    @JsonProperty("service")
    private String service;

    @JsonProperty("text")
    private String text;
}
