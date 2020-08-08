package com.epam.messenger.service;

import com.epam.messenger.model.dto.ContactDTO;
import com.epam.messenger.model.dto.LoginUserDTO;
import com.epam.messenger.model.dto.LoginUserResponseDTO;
import com.epam.messenger.model.User;
import com.epam.messenger.model.dto.RegisterUserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User findById(Integer userId);

    ContactDTO findByPhoneNumber(String phoneNumber);

    ContactDTO addContactToUser(Integer userId, String contactPhoneNumber);

    LoginUserResponseDTO login(LoginUserDTO loginUserDTO);

    Integer register(RegisterUserDTO registerUserDTO);

    User updateUserInfo(Integer userId, String userName);
}
