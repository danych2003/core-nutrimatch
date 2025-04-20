package ee.danych.nutrimatch.api;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseUser {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarBase64;
}
