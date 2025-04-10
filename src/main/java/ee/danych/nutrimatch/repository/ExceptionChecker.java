package ee.danych.nutrimatch.repository;

public interface ExceptionChecker {
    void method();

    default boolean isExceptionSafe() {
        try {
            method();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
