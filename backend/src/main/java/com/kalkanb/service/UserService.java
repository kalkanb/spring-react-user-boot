package com.kalkanb.service;

import com.kalkanb.dto.AuthenticatedUserDto;
import com.kalkanb.dto.AuthenticationDto;
import com.kalkanb.dto.UserDto;
import com.kalkanb.entity.BlacklistedTokenEntity;
import com.kalkanb.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Boolean> register(AuthenticationDto authenticationDto);

    UserEntity insertUser(AuthenticationDto authenticationDto);

    AuthenticatedUserDto login(AuthenticationDto authenticationDto);

    ResponseEntity<?> logout(String token);

    BlacklistedTokenEntity blacklistToken(String token);

    UserDto getUser();
}
