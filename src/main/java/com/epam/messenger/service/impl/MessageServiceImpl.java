package com.epam.messenger.service.impl;

import com.epam.messenger.model.Message;
import com.epam.messenger.repository.MessageRepository;
import com.epam.messenger.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(final Message message) {
        return messageRepository.save(message);
    }
}
