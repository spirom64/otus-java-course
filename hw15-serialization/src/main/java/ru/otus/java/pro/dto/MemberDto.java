package ru.otus.java.pro.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberDto {
    @JsonProperty("first")
    private String first;

    public int getHandleId() {
        return handleId;
    }

    @JsonProperty("handle_id")
    private int handleId;

    @JsonProperty("image_path")
    private String imagePath;

    public String getLast() {
        return last;
    }

    @JsonProperty("last")
    private String last;

    @JsonProperty("middle")
    private String middle;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("service")
    private String service;

    @JsonProperty("thumb_path")
    private String thumbPath;
}
