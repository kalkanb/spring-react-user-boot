package com.kalkanb.service;

import com.kalkanb.authority.jwt.JwtUtils;
import com.kalkanb.dto.AuthenticatedUserDto;
import com.kalkanb.dto.AuthenticationDto;
import com.kalkanb.dto.UserDto;
import com.kalkanb.entity.BlacklistedTokenEntity;
import com.kalkanb.entity.RoleEntity;
import com.kalkanb.entity.UserEntity;
import com.kalkanb.enums.RoleEnum;
import com.kalkanb.exception.*;
import com.kalkanb.mapper.UserMapper;
import com.kalkanb.repository.BlacklistedTokenRepository;
import com.kalkanb.repository.RoleRepository;
import com.kalkanb.repository.UserRepository;
import com.kalkanb.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USERNAME = "username";
    private static final String INVALID_PARAMS = "Invalid parameters";
    private static final String EMAIL_PASSWD_MISMATCH = "Wrong email or password";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BlacklistedTokenRepository blacklistedTokenRepository;
    private JwtUtils jwtUtils;
    private UserMapper userMapper;

    @Override
    public ResponseEntity<Boolean> register(AuthenticationDto authenticationDto) {
        UserEntity userEntity = insertUser(authenticationDto);
        if (userEntity == null) {
            throw new ProjectException("User could not be registered");
        }
        return new ResponseEntity<>(Boolean.TRUE, new HttpHeaders(), HttpStatus.CREATED);
    }

    @Override
    public UserEntity insertUser(AuthenticationDto authenticationDto) {
        if (authenticationDto == null || StringUtils.isEmpty(authenticationDto.getUsername())
                || StringUtils.isBlank(authenticationDto.getPassword())) {
            throw new ProjectValidationException("Email or password cannot be empty");
        }
        authenticationDto.setUsername(authenticationDto.getUsername().trim());
        if (StringUtils.isBlank(authenticationDto.getUsername())) {
            Map<String, String> fields = new HashMap<>();
            fields.put(USERNAME, "Username cannot be all whitespace");
            throw new ProjectArgumentValidationException(INVALID_PARAMS, fields);
        }
        if (userRepository.findByUsername(authenticationDto.getUsername()).isPresent()) {
            Map<String, String> fields = new HashMap<>();
            fields.put(USERNAME, "There is already a registered user with this username");
            throw new ProjectArgumentValidationException(INVALID_PARAMS, fields);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authenticationDto.getUsername());
        userEntity.setPassword(BCrypt.hashpw(authenticationDto.getPassword(), BCrypt.gensalt()));
        userEntity.setRoles(new HashSet<>());
        RoleEntity userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new ProjectNotFoundException("User role could not be found"));
        userEntity.getRoles().add(userRole);

        UserEntity savedEntity;
        try {
            savedEntity = userRepository.save(userEntity);
        } catch (Exception ex) {
            throw new ProjectException(ex.getMessage(), ex);
        }

        return savedEntity;
    }

    @Override
    public AuthenticatedUserDto login(AuthenticationDto authenticationDto) {
        if (authenticationDto == null || StringUtils.isBlank(authenticationDto.getUsername())
                || StringUtils.isBlank(authenticationDto.getPassword())) {
            throw new ProjectValidationException("Email or password cannot be empty");
        }
        UserEntity userEntity = userRepository.findByUsername(authenticationDto.getUsername())
                .orElseThrow(() -> {
                    Map<String, String> fields = new HashMap<>();
                    fields.put(USERNAME, EMAIL_PASSWD_MISMATCH);
                    fields.put("password", EMAIL_PASSWD_MISMATCH);
                    throw new ProjectAuthenticationValidationException(INVALID_PARAMS, fields);
                });

        if (!BCrypt.checkpw(authenticationDto.getPassword(), userEntity.getPassword())) {
            Map<String, String> fields = new HashMap<>();
            fields.put(USERNAME, EMAIL_PASSWD_MISMATCH);
            fields.put("password", EMAIL_PASSWD_MISMATCH);
            throw new ProjectAuthenticationValidationException(INVALID_PARAMS, fields);
        }

        String token = jwtUtils.generateJwtToken(authenticationDto.getUsername());

        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto();
        authenticatedUserDto.setUser(userMapper.toDto(userEntity));
        authenticatedUserDto.setToken(token);

        return authenticatedUserDto;
    }

    @Override
    public ResponseEntity<?> logout(String token) {
        BlacklistedTokenEntity blacklistedTokenEntity = blacklistToken(token);
        if (blacklistedTokenEntity == null) {
            throw new ProjectException("Logout failed");
        }

        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @Override
    public BlacklistedTokenEntity blacklistToken(String token) {
        BlacklistedTokenEntity blacklistedTokenEntity = new BlacklistedTokenEntity();
        blacklistedTokenEntity.setToken(token);
        Date expirationDate = jwtUtils.getExpirationDateFromJwtToken(token);
        blacklistedTokenEntity.setExpirationDate(
                DateTimeUtils.convertToLocalDateTimeViaInstant(expirationDate));

        BlacklistedTokenEntity savedEntity;
        try {
            savedEntity = blacklistedTokenRepository.save(blacklistedTokenEntity);
        } catch (Exception ex) {
            throw new ProjectException(ex.getMessage(), ex);
        }
        return savedEntity;
    }

    @Override
    public UserDto getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new ProjectNotFoundException("User not found! username: " + username));
        return userMapper.toDto(userEntity);
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBlacklistedTokenRepository(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
