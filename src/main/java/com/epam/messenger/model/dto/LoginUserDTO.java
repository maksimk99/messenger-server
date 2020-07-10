package com.epam.messenger.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {

    private String phoneNumber;
    private String password;
}
