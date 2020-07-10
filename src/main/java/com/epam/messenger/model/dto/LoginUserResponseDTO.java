package com.epam.messenger.model.dto;

import com.epam.messenger.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginUserResponseDTO {

    private Integer userId;
    private String userName;
    private String phoneNumber;
    private String password;
    private String avatarUrl;
    @JsonIgnoreProperties({"contacts", "password"})
    private List<User> contacts;
    private List<ChatDTO> chats;

    public static LoginUserResponseDTO build(final User user, final List<ChatDTO> chats) {
        LoginUserResponseDTO userResponseDTO = new LoginUserResponseDTO();
        userResponseDTO.setUserId(user.getUserId());
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setPassword(user.getPassword());
        userResponseDTO.setAvatarUrl(user.getAvatarUrl());
        userResponseDTO.setContacts(user.getContacts());
        userResponseDTO.setChats(chats);
        return userResponseDTO;
    }
}
