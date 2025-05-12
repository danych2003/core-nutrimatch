package ee.danych.nutrimatch.model.dto;

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
