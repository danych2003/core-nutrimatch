package ee.danych.nutrimatch.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {
    USER_ALREADY_EXISTS("userAlreadyExists"),
    USER_NOT_FOUND("userNotFound"),
    INVALID_PASSWORD("invalidPassword"),
    BAD_REQUEST("badRequest"),
    PRODUCT_NOT_FOUND("productNotFound");

    private final String errorCode;
}
