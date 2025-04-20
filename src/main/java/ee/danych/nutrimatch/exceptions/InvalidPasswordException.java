package ee.danych.nutrimatch.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String username) {
        super("Invalid password for user with " + username + " username");
    }
}
