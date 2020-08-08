package com.epam.messenger.controller;

import com.epam.messenger.model.User;
import com.epam.messenger.model.dto.ContactDTO;
import com.epam.messenger.model.dto.LoginUserDTO;
import com.epam.messenger.model.dto.LoginUserResponseDTO;
import com.epam.messenger.model.dto.RegisterUserDTO;
import com.epam.messenger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginUserResponseDTO login(@RequestBody LoginUserDTO loginUserDTO) {
        logger.info("LOGIN USER: " + loginUserDTO);
        LoginUserResponseDTO user = userService.login(loginUserDTO);
        logger.info("LOGIN RESULT: " + user);
        return user;
    }

    @PostMapping("/register")
    public Integer register(@RequestBody RegisterUserDTO registerUserDTO) {
        logger.info("registerUserDTO: " + registerUserDTO);
        return userService.register(registerUserDTO);
    }

    @GetMapping("/user")
    public ContactDTO findContactByPhoneNumber(@RequestParam final String phoneNumber, @RequestParam final Integer userId) {
        return userService.addContactToUser(userId, phoneNumber);
    }

    @PostMapping("/user/{userId}/update")
    public User updateUserInfo(@PathVariable Integer userId,
                               @RequestParam(value = "userName", required = false) String userName) {
        return userService.updateUserInfo(userId, userName);
    }
}
