package com.epam.messenger.model.dto;

import com.epam.messenger.model.Chat;
import com.epam.messenger.model.Message;
import com.epam.messenger.model.User;
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

    public Message convertToMessage() {
        User sender = new User();
        sender.setUserId(this.senderId);
        Chat chat = new Chat();
        chat.setChatId(this.chatId);

        Message message = new Message();
        message.setMessage(this.message);
        message.setDateSent(this.dateSent);
        message.setSender(sender);
        message.setChat(chat);
        return message;
    }
}
