package com.epam.messenger.model.dto;

import com.epam.messenger.model.Chat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true)
@Getter
@Setter
public class ChatBrokerDTO {

    private Integer chatId;
    private String chatName;
    private String avatarUrl;
    private List<ContactDTO> participants;

    public static ChatBrokerDTO build(final Chat chat) {
        return new ChatBrokerDTO()
            .setChatId(chat.getChatId())
            .setChatName(chat.getChatName())
            .setAvatarUrl(chat.getAvatarUrl())
            .setParticipants(chat.getUsers().stream().map(ContactDTO::build).collect(Collectors.toList()));
    }
}
