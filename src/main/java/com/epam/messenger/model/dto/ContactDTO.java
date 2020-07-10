package com.epam.messenger.model.dto;

import com.epam.messenger.model.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ContactDTO {
    private Integer contactId;
    private String contactName;
    private String phoneNumber;
    private String avatarUrl;
    private Timestamp lastSeen;

    public static ContactDTO build(final User user) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setContactId(user.getUserId());
        contactDTO.setContactName(user.getUserName());
        contactDTO.setPhoneNumber(user.getPhoneNumber());
        contactDTO.setAvatarUrl(user.getAvatarUrl());
        contactDTO.setLastSeen(user.getLastSeen());
        return contactDTO;
    }
}
