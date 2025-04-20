package ee.danych.nutrimatch.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    public ErrorResponse(ErrorCodes errorCode, String errorMessage) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorMessage;
    }

    String errorCode;
    String errorMessage;
}
