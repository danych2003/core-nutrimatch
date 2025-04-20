package ee.danych.nutrimatch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

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
