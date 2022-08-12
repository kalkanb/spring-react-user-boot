package com.kalkanb.controller;

import com.kalkanb.dto.AuthenticatedUserDto;
import com.kalkanb.dto.AuthenticationDto;
import com.kalkanb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class AuthenticationController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody @Valid AuthenticationDto authenticationDto) {
        return userService.register(authenticationDto);
    }

    @PostMapping("/login")
    public AuthenticatedUserDto login(@RequestBody @Valid AuthenticationDto authenticationDto) {
        return userService.login(authenticationDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-User-Token") String token) {
        return userService.logout(token);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
