package ee.danych.nutrimatch.model.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @NotNull
    @Size(min = 5, max = 12)
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    @NotNull
    @Size(min = 5, max = 20)
    private String password;
}
