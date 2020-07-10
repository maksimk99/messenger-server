package com.epam.messenger.service;

import com.epam.messenger.model.dto.ContactDTO;
import com.epam.messenger.model.dto.LoginUserDTO;
import com.epam.messenger.model.dto.LoginUserResponseDTO;
import com.epam.messenger.model.User;
import com.epam.messenger.model.dto.RegisterUserDTO;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Integer userId);

    ContactDTO findByPhoneNumber(String phoneNumber);

    LoginUserResponseDTO login(LoginUserDTO loginUserDTO);

    Integer register(RegisterUserDTO registerUserDTO);
}
