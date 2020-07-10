package com.epam.messenger.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://192.168.43.125:8100")
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
    public ContactDTO findContactByPhoneNumber(@RequestParam final String phoneNumber) {
        logger.info("findContactByPhoneNumber: phoneNumber = " + phoneNumber);
        ContactDTO contactDTO = userService.findByPhoneNumber(phoneNumber);
        logger.info("RESPONSE: " + contactDTO);
        return contactDTO;
    }
}
