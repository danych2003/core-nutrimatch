package ee.danych.nutrimatch.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.USER_ALREADY_EXISTS, exception.getMessage());
        log.error("Error: {}, Username: {}", errorResponse.errorCode, errorResponse.errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.USER_NOT_FOUND, exception.getMessage());
        log.error("Error: {}, Username: {}", errorResponse.errorCode, errorResponse.errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPasswordException(InvalidPasswordException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.INVALID_PASSWORD, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.PRODUCT_NOT_FOUND, exception.getMessage());
        log.error("Error: {}, ProductId: {}", errorResponse.errorCode, errorResponse.errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<?> handleRecipeNotFoundException(RecipeNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.RECIPE_NOT_FOUND, exception.getMessage());
        log.error("Error: {}, RecipeId: {}", errorResponse.errorCode, errorResponse.errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(XlsxFileNotFoundException.class)
    public ResponseEntity<?> handleXlsxFileNotFoundException(XlsxFileNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.XLSX_FILE_NOT_FOUND, exception.getMessage());
        log.error("Error: {}, Message: {}, FilePath: {}", errorResponse.errorCode, errorResponse.errorMessage, exception.getFilePath());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.BAD_REQUEST, errors.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
