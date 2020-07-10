package com.epam.messenger.model.dto;

import com.epam.messenger.model.Chat;
import com.epam.messenger.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true)
@Getter
@Setter
public class ChatDTO {
    private Integer chatId;
    private String chatName;
    private String avatarUrl;
    private List<Integer> members;

    public Chat convertToChat() {
        return new Chat()
                .setChatName(this.chatName)
                .setAvatarUrl(this.avatarUrl)
                .setUsers(members.stream().map(memberId -> new User().setUserId(memberId)).collect(Collectors.toList()));
    }

    public static ChatDTO build(final Chat chat) {
        return new ChatDTO()
            .setChatId(chat.getChatId())
            .setChatName(chat.getChatName())
            .setAvatarUrl(chat.getAvatarUrl())
            .setMembers(chat.getUsers().stream().map(User::getUserId).collect(Collectors.toList()));
    }
}
