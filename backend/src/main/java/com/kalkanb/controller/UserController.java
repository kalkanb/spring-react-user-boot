package com.kalkanb.controller;

import com.kalkanb.dto.UserDto;
import com.kalkanb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    @GetMapping
    public UserDto getUser() {
        return userService.getUser();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
