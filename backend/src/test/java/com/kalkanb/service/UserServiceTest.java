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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String INVALID_PARAMS = "Invalid parameters";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String EMAIL_PASSWD_MISMATCH = "Wrong email or password";

    @InjectMocks
    @Spy
    UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BlacklistedTokenRepository blacklistedTokenRepository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserMapper userMapper;

    @Test
    public void register_error() {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setUsername("user");
        authenticationDto.setPassword("pwd");

        Mockito.doReturn(null).when(userService).insertUser(authenticationDto);

        ProjectException exception = Assertions.assertThrows(ProjectException.class,
                () -> userService.register(authenticationDto));
        Assertions.assertEquals("User could not be registered", exception.getMessage());
    }

    @Test
    public void register_success() {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setUsername("user");
        authenticationDto.setPassword("pwd");

        UserEntity userEntity = new UserEntity();
        Mockito.doReturn(userEntity).when(userService).insertUser(authenticationDto);

        ResponseEntity<Boolean> actual = userService.register(authenticationDto);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getHeaders());
        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        Assertions.assertNotNull(actual.getBody());
        Assertions.assertEquals(Boolean.TRUE, actual.getBody());

        Mockito.verify(userService, Mockito.times(1))
                .insertUser(authenticationDto);
    }

    @Test
    public void insertUser_parameter_test() {
        ProjectValidationException exception1 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.insertUser(null));
        Assertions.assertEquals("Email or password cannot be empty", exception1.getMessage());

        AuthenticationDto authenticationDto2 = new AuthenticationDto();
        ProjectValidationException exception2 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.insertUser(authenticationDto2));
        Assertions.assertEquals("Email or password cannot be empty", exception2.getMessage());

        AuthenticationDto authenticationDto3 = new AuthenticationDto();
        authenticationDto3.setUsername("");
        authenticationDto3.setPassword("pwd");
        ProjectValidationException exception3 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.insertUser(authenticationDto3));
        Assertions.assertEquals("Email or password cannot be empty", exception3.getMessage());

        AuthenticationDto authenticationDto4 = new AuthenticationDto();
        authenticationDto4.setUsername("user");
        authenticationDto4.setPassword("");
        ProjectValidationException exception4 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.insertUser(authenticationDto4));
        Assertions.assertEquals("Email or password cannot be empty", exception4.getMessage());

        AuthenticationDto authenticationDto5 = new AuthenticationDto();
        authenticationDto5.setUsername("   ");
        authenticationDto5.setPassword("pwd");
        ProjectArgumentValidationException exception5 = Assertions.assertThrows(ProjectArgumentValidationException.class,
                () -> userService.insertUser(authenticationDto5));
        Assertions.assertEquals(INVALID_PARAMS, exception5.getMessage());
        Assertions.assertNotNull(exception5.getFields());
        Assertions.assertFalse(exception5.getFields().isEmpty());
        Assertions.assertTrue(exception5.getFields().containsKey("username"));
        Assertions.assertEquals("Username cannot be all whitespace", exception5.getFields().get("username"));

        AuthenticationDto authenticationDto6 = new AuthenticationDto();
        authenticationDto6.setUsername("existingUser");
        authenticationDto6.setPassword("pwd");

        UserEntity existingUser = new UserEntity();
        Mockito.when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        ProjectArgumentValidationException exception6 = Assertions.assertThrows(ProjectArgumentValidationException.class,
                () -> userService.insertUser(authenticationDto6));
        Assertions.assertEquals(INVALID_PARAMS, exception6.getMessage());
        Assertions.assertNotNull(exception6.getFields());
        Assertions.assertFalse(exception6.getFields().isEmpty());
        Assertions.assertTrue(exception6.getFields().containsKey("username"));
        Assertions.assertEquals("There is already a registered user with this username",
                exception6.getFields().get("username"));
    }

    @Test
    public void insertUser_role_not_found_error() {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setUsername("user");
        authenticationDto.setPassword("pwd");

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.empty());

        ProjectNotFoundException exception = Assertions.assertThrows(ProjectNotFoundException.class,
                () -> userService.insertUser(authenticationDto));
        Assertions.assertEquals("User role could not be found", exception.getMessage());
    }

    @Test
    public void insertUser_db_error() {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setUsername("user");
        authenticationDto.setPassword("pwd");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName(RoleEnum.ROLE_USER);

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(roleEntity));
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Db error"));

        ProjectException exception = Assertions.assertThrows(ProjectException.class,
                () -> userService.insertUser(authenticationDto));
        Assertions.assertEquals("Db error", exception.getMessage());
    }

    @Test
    public void insertUser_success() {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setUsername("user.name");
        authenticationDto.setPassword("12345678Ab.");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName(RoleEnum.ROLE_USER);

        Mockito.when(userRepository.findByUsername("user.name")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(roleEntity));
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity user = (UserEntity) invocation.getArguments()[0];
                    user.setId(1L);
                    return user;
                });

        UserEntity actual = userService.insertUser(authenticationDto);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("user.name", actual.getUsername());
        Assertions.assertTrue(BCrypt.checkpw("12345678Ab.", actual.getPassword()));
        Assertions.assertNotNull(actual.getRoles());
        Assertions.assertFalse(actual.getRoles().isEmpty());
        Assertions.assertEquals(1, actual.getRoles().size());

        RoleEntity actualRole = actual.getRoles().stream().findFirst().orElse(null);
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(1L, actualRole.getId());
        Assertions.assertEquals(RoleEnum.ROLE_USER, actualRole.getName());
    }

    @Test
    public void login_parameter_test() {
        ProjectValidationException exception1 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.login(null));
        Assertions.assertEquals("Email or password cannot be empty", exception1.getMessage());

        AuthenticationDto authenticationDto2 = new AuthenticationDto();
        ProjectValidationException exception2 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.login(authenticationDto2));
        Assertions.assertEquals("Email or password cannot be empty", exception2.getMessage());

        AuthenticationDto authenticationDto3 = new AuthenticationDto();
        authenticationDto3.setUsername("");
        authenticationDto3.setPassword("pwd");
        ProjectValidationException exception3 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.login(authenticationDto3));
        Assertions.assertEquals("Email or password cannot be empty", exception3.getMessage());

        AuthenticationDto authenticationDto4 = new AuthenticationDto();
        authenticationDto4.setUsername("user");
        authenticationDto4.setPassword("");
        ProjectValidationException exception4 = Assertions.assertThrows(ProjectValidationException.class,
                () -> userService.login(authenticationDto4));
        Assertions.assertEquals("Email or password cannot be empty", exception4.getMessage());
    }

    @Test
    public void login_auth_failed_error() {
        //non-existing username
        AuthenticationDto authenticationDto1 = new AuthenticationDto();
        authenticationDto1.setUsername("user");
        authenticationDto1.setPassword("pwd");

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        ProjectAuthenticationValidationException exception1 = Assertions
                .assertThrows(ProjectAuthenticationValidationException.class,
                        () -> userService.login(authenticationDto1));
        Assertions.assertEquals(INVALID_PARAMS, exception1.getMessage());
        Assertions.assertNotNull(exception1.getFields());
        Assertions.assertFalse(exception1.getFields().isEmpty());
        Assertions.assertTrue(exception1.getFields().containsKey("username"));
        Assertions.assertTrue(exception1.getFields().containsKey("password"));
        Assertions.assertEquals(EMAIL_PASSWD_MISMATCH, exception1.getFields().get("username"));
        Assertions.assertEquals(EMAIL_PASSWD_MISMATCH, exception1.getFields().get("password"));

        //username-password mismatch
        Mockito.reset(userRepository);
        AuthenticationDto authenticationDto2 = new AuthenticationDto();
        authenticationDto2.setUsername("user");
        authenticationDto2.setPassword("pwd");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("user");
        userEntity.setPassword(BCrypt.hashpw("pwd1", BCrypt.gensalt()));
        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(userEntity));

        ProjectAuthenticationValidationException exception2 = Assertions
                .assertThrows(ProjectAuthenticationValidationException.class,
                        () -> userService.login(authenticationDto2));
        Assertions.assertEquals(INVALID_PARAMS, exception2.getMessage());
        Assertions.assertNotNull(exception2.getFields());
        Assertions.assertFalse(exception2.getFields().isEmpty());
        Assertions.assertTrue(exception2.getFields().containsKey("username"));
        Assertions.assertTrue(exception2.getFields().containsKey("password"));
        Assertions.assertEquals(EMAIL_PASSWD_MISMATCH, exception2.getFields().get("username"));
        Assertions.assertEquals(EMAIL_PASSWD_MISMATCH, exception2.getFields().get("password"));
    }

    @Test
    public void login_success() {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setUsername("username");
        authenticationDto.setPassword("12345Ab.");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.setPassword(BCrypt.hashpw("12345Ab.", BCrypt.gensalt()));

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("username");

        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));

        Mockito.when(jwtUtils.generateJwtToken(authenticationDto.getUsername()))
                .thenReturn("MockedGeneratedToken");

        Mockito.when(userMapper.toDto(userEntity)).thenReturn(userDto);

        AuthenticatedUserDto actual = userService.login(authenticationDto);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getUser());
        Assertions.assertEquals(1L, actual.getUser().getId());
        Assertions.assertEquals("username", actual.getUser().getUsername());
        Assertions.assertEquals("MockedGeneratedToken", actual.getToken());
    }

    @Test
    public void logout_error() {
        Mockito.doReturn(null).when(userService).blacklistToken("TestToken");

        ProjectException exception = Assertions.assertThrows(ProjectException.class,
                () -> userService.logout("TestToken"));
        Assertions.assertEquals("Logout failed", exception.getMessage());
    }

    @Test
    public void logout_success() {
        BlacklistedTokenEntity blacklistedTokenEntity = new BlacklistedTokenEntity();
        Mockito.doReturn(blacklistedTokenEntity).when(userService).blacklistToken("TestToken");

        ResponseEntity<?> actual = userService.logout("TestToken");
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getHeaders());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        Assertions.assertNull(actual.getBody());

        Mockito.verify(userService, Mockito.times(1))
                .blacklistToken("TestToken");
    }

    @Test
    public void blacklistToken_db_error() {
        LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(1, ChronoUnit.MINUTES));
        Date expirationDate = DateTimeUtils.convertToDate(dateTime);

        Mockito.when(jwtUtils.getExpirationDateFromJwtToken("TestToken"))
                .thenReturn(expirationDate);

        Mockito.when(blacklistedTokenRepository.save(Mockito.any(BlacklistedTokenEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Db error"));

        ProjectException exception = Assertions.assertThrows(ProjectException.class,
                () -> userService.blacklistToken("TestToken"));
        Assertions.assertEquals("Db error", exception.getMessage());
    }

    @Test
    public void blacklistToken_success() {
        LocalDateTime expirationLocalDateTime = LocalDateTime.now().plus(Duration.of(1, ChronoUnit.MINUTES));
        Date expirationDate = DateTimeUtils.convertToDate(expirationLocalDateTime);

        Mockito.when(jwtUtils.getExpirationDateFromJwtToken("TestToken"))
                .thenReturn(expirationDate);

        Mockito.when(blacklistedTokenRepository.save(Mockito.any(BlacklistedTokenEntity.class)))
                .thenAnswer(invocation -> {
                    BlacklistedTokenEntity blacklistedTokenEntity =
                            (BlacklistedTokenEntity) invocation.getArguments()[0];
                    blacklistedTokenEntity.setId(1L);
                    return blacklistedTokenEntity;
                });

        BlacklistedTokenEntity actual = userService.blacklistToken("TestToken");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("TestToken", actual.getToken());
        long differenceInSeconds = ChronoUnit.SECONDS.between(expirationLocalDateTime, actual.getExpirationDate());
        Assertions.assertEquals(0L, differenceInSeconds);
    }

    @Test
    public void getUser_error() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        ProjectNotFoundException exception = Assertions.assertThrows(ProjectNotFoundException.class,
                () -> userService.getUser());
        Assertions.assertEquals("User not found! username: username", exception.getMessage());
    }

    @Test
    public void getUser_success() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("username");

        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapper.toDto(userEntity)).thenReturn(userDto);

        UserDto actual = userService.getUser();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("username", actual.getUsername());
    }
}
