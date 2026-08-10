package com.rahul.learning.javaguide.emsbackend.exceptions;

import com.rahul.learning.javaguide.emsbackend.dtos.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DepartmentDeletionException.class,
            EmployeeCreationException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleConflictExceptions(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(
                        HttpStatus.CONFLICT,
                        ex.getMessage(),
                        request
                ));
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserRegistrationException(UserRegistrationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request
                ));
    }

    @ExceptionHandler(UserLoginFailedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserLoginFailedException(UserLoginFailedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse(
                        HttpStatus.UNAUTHORIZED,
                        ex.getMessage(),
                        request
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        ErrorResponseDTO response = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request
                );

        response.setValidationErrors(validationErrors);
        return ResponseEntity.badRequest().body(response);
    }

    private ErrorResponseDTO buildErrorResponse(HttpStatus httpStatus, String errorMessage, HttpServletRequest request) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                errorMessage,
                request.getRequestURI(),
                null
        );
    }
}