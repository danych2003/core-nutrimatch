package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.exceptions.InvalidPasswordException;
import ee.danych.nutrimatch.exceptions.UserAlreadyExistsException;
import ee.danych.nutrimatch.exceptions.UserNotFoundException;
import ee.danych.nutrimatch.model.dto.auth.RegisterUserDto;
import ee.danych.nutrimatch.model.entity.User;
import ee.danych.nutrimatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    public ResponseEntity<?> register(RegisterUserDto registerUserDto) {
        if (userRepository.existsUserByUsername(registerUserDto.getUsername())) {
            throw new UserAlreadyExistsException(registerUserDto.getUsername());
        }
        User user = createUserFromDto(registerUserDto);

        userRepository.save(user);
        log.info("User with username {} successfully registered.", registerUserDto.getUsername());
        return ResponseEntity.ok("User successfully created");
    }

    public ResponseEntity<?> verify(RegisterUserDto registerUserDto) {
        if (!userRepository.existsUserByUsername(registerUserDto.getUsername())) {
            throw new UserNotFoundException(registerUserDto.getUsername());
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registerUserDto.getUsername(),
                            registerUserDto.getPassword())
            );
            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(jwtService.generateToken(registerUserDto.getUsername()));
            }
        } catch (BadCredentialsException exception) {
            throw new InvalidPasswordException(registerUserDto.getUsername());
        }

        return ResponseEntity.notFound().build();
    }

    public User findUser(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username)).orElseThrow(() -> new UserNotFoundException(username));
    }

    public User updateUserAvatar(String username, String avatarBase64) {
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException(username));
        user.setAvatarBase64(avatarBase64);
        return userRepository.save(user);
    }

    private User createUserFromDto(RegisterUserDto registerUserDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .username(registerUserDto.getUsername())
                .passwordHash(passwordEncoder.encode(registerUserDto.getPassword()))
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .email(registerUserDto.getEmail())
                .build();
    }
}
