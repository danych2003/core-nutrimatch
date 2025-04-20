package ee.danych.nutrimatch.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {
    USER_ALREADY_EXISTS("userAlreadyExists"),
    BAD_REQUEST("badRequest");

    private final String errorCode;
}
