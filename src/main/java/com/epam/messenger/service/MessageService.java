package com.epam.messenger.service;

import com.epam.messenger.model.Message;

public interface MessageService {
    Message addMessage(Message message);
    Message findById(Integer messageId);
}
