package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.exceptions.InvalidPasswordException;
import ee.danych.nutrimatch.exceptions.UserAlreadyExistsException;
import ee.danych.nutrimatch.exceptions.UserNotFoundException;
import ee.danych.nutrimatch.model.dto.auth.RegisterUserDto;
import ee.danych.nutrimatch.model.entity.User;
import ee.danych.nutrimatch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JWTService.class);
        userService = new UserService(userRepository, authenticationManager, jwtService);
    }

    @Test
    void register_whenUserAlreadyExists_shouldThrowException() {
        RegisterUserDto dto = new RegisterUserDto("user", "pass", "f", "l", "email");
        when(userRepository.existsUserByUsername("user")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(dto));
    }

    @Test
    void register_whenUserDoesNotExist_shouldSaveAndReturnSuccess() {
        RegisterUserDto dto = new RegisterUserDto("newuser", "pass", "f", "l", "email");
        when(userRepository.existsUserByUsername("newuser")).thenReturn(false);

        ResponseEntity<?> response = userService.register(dto);

        verify(userRepository).save(any(User.class));
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void verify_whenUserNotFound_shouldThrowException() {
        RegisterUserDto dto = new RegisterUserDto("nouser", "pass", null, null, null);
        when(userRepository.existsUserByUsername("nouser")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.verify(dto));
    }

    @Test
    void verify_whenCredentialsValid_shouldReturnJwt() {
        RegisterUserDto dto = new RegisterUserDto("user", "pass", null, null, null);
        when(userRepository.existsUserByUsername("user")).thenReturn(true);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateToken("user")).thenReturn("mock-jwt");

        ResponseEntity<?> response = userService.verify(dto);

        assertEquals("mock-jwt", response.getBody());
    }

    @Test
    void verify_whenCredentialsInvalid_shouldThrowInvalidPasswordException() {
        RegisterUserDto dto = new RegisterUserDto("user", "wrong", null, null, null);
        when(userRepository.existsUserByUsername("user")).thenReturn(true);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad creds"));

        assertThrows(InvalidPasswordException.class, () -> userService.verify(dto));
    }

    @Test
    void findUser_whenUserExists_shouldReturnUser() {
        User user = new User();
        when(userRepository.findByUsername("john")).thenReturn(user);

        User result = userService.findUser("john");

        assertEquals(user, result);
    }

    @Test
    void findUser_whenUserNotFound_shouldThrowException() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.findUser("ghost"));
    }

    @Test
    void updateUserAvatar_whenUserExists_shouldUpdateAndSaveAvatar() {
        User user = new User();
        user.setUsername("alex");
        when(userRepository.findByUsername("alex")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUserAvatar("alex", "base64avatar");

        assertEquals("base64avatar", result.getAvatarBase64());
    }

    @Test
    void updateUserAvatar_whenUserNotFound_shouldThrowException() {
        when(userRepository.findByUsername("missing")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.updateUserAvatar("missing", "avatar"));
    }
}
