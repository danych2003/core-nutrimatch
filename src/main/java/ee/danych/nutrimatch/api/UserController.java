package ee.danych.nutrimatch.api;

import ee.danych.nutrimatch.model.dto.ResponseUser;
import ee.danych.nutrimatch.model.dto.auth.RegisterUserDto;
import ee.danych.nutrimatch.model.entity.User;
import ee.danych.nutrimatch.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterUserDto user) {
        log.debug("User with username {} successfully logged in.", user.getUsername());
        return userService.verify(user);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        ResponseUser user = getResponseUser(userService.findUser(username));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/avatar/{username}")
    public ResponseEntity<?> updateUserAvatar(@PathVariable String username, @RequestBody String photoBase64) {
        ResponseUser user = getResponseUser(userService.updateUserAvatar(username, photoBase64));
        return ResponseEntity.ok(user);
    }

    private ResponseUser getResponseUser(User user) {
        return ResponseUser.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .avatarBase64(user.getAvatarBase64())
                .build();
    }
}
