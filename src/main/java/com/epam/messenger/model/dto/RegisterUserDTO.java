package com.epam.messenger.model.dto;

import com.epam.messenger.model.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class RegisterUserDTO {

    private String userName;
    private String phoneNumber;
    private String password;

    public User convertToUser() {
        User user = new User();
        user.setPhoneNumber(this.phoneNumber);
        user.setUserName(this.userName);
        user.setPassword(this.password);
        user.setLastSeen(new Timestamp(new Date().getTime()));
        return user;
    }
}
