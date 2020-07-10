package com.epam.messenger.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MessageDTO {

    private String message;
    private Timestamp dateSent;
    private Integer chatId;
    private Integer senderId;
}
