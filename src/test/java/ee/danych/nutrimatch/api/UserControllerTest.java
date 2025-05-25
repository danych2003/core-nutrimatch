package ee.danych.nutrimatch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.danych.nutrimatch.config.SecurityConfig;
import ee.danych.nutrimatch.model.dto.auth.RegisterUserDto;
import ee.danych.nutrimatch.model.entity.User;
import ee.danych.nutrimatch.service.JWTService;
import ee.danych.nutrimatch.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("POST /register returns 200")
    void register() throws Exception {
        RegisterUserDto dto = new RegisterUserDto("username", "pass", "user@email.com", "John", "Doe1234.23");
        when(userService.register(any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /login returns 200 and logs in")
    void login() throws Exception {
        RegisterUserDto dto = new RegisterUserDto("user", "pass", null, null, null);
        when(userService.verify(any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /user/{username} returns user")
    void getUser() throws Exception {
        User user = User.builder()
                .username("user")
                .firstName("John")
                .lastName("Doe")
                .email("user@email.com")
                .avatarBase64("base64")
                .build();
        when(userService.findUser("user")).thenReturn(user);

        mockMvc.perform(get("/api/user/user").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.avatarBase64").value("base64"));
    }

    @Test
    @DisplayName("PUT /user/avatar/{username} updates avatar")
    void updateUserAvatar() throws Exception {
        String base64 = "newBase64";
        User user = User.builder()
                .username("user")
                .firstName("John")
                .lastName("Doe")
                .email("user@email.com")
                .avatarBase64(base64)
                .build();

        when(userService.updateUserAvatar(eq("user"), eq(base64))).thenReturn(user);

        mockMvc.perform(put("/api/user/avatar/user")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(base64))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarBase64").value(base64));
    }
}
