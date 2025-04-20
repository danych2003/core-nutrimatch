package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.dto.UserDTO;
import ee.danych.nutrimatch.entity.User;
import ee.danych.nutrimatch.exceptions.InvalidPasswordException;
import ee.danych.nutrimatch.exceptions.UserAlreadyExistsException;
import ee.danych.nutrimatch.exceptions.UserNotFoundException;
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


    public ResponseEntity<?> register(UserDTO userDTO) {
        if (userRepository.existsUserByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException(userDTO.getUsername());
        }
        User user = createUserFromDTO(userDTO);

        userRepository.save(user);
        return ResponseEntity.ok("User successfully created");
    }

    public ResponseEntity<?> verify(UserDTO userDTO) {
        if (!userRepository.existsUserByUsername(userDTO.getUsername())) {
            throw new UserNotFoundException(userDTO.getUsername());
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(),
                            userDTO.getPassword())
            );
            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(jwtService.generateToken(userDTO.getUsername()));
            }
        } catch (BadCredentialsException exception) {
            throw new InvalidPasswordException(userDTO.getUsername());
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

    private User createUserFromDTO(UserDTO userDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .username(userDTO.getUsername())
                .passwordHash(passwordEncoder.encode(userDTO.getPassword()))
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .build();
    }
}
