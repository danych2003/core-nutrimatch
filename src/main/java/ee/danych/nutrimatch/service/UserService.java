package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.entity.User;
import ee.danych.nutrimatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    public ResponseEntity<?> register(User user) {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            return new ResponseEntity<>("User with " + user.getUsername() + " already exists", HttpStatus.BAD_REQUEST);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        userRepository.save(user);
        return ResponseEntity.ok("User successfully created");
    }

    public ResponseEntity<?> verify(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPasswordHash()));

        if(authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(user.getUsername()));
        }
        return ResponseEntity.notFound().build();
    }
}
