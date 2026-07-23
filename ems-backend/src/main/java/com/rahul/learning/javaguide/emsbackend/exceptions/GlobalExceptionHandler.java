package com.rahul.learning.javaguide.emsbackend.exceptions;

import com.rahul.learning.javaguide.emsbackend.dtos.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DepartmentDeletionException.class,
            EmployeeCreationException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleConflictExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

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

    private ErrorResponseDTO buildErrorResponse(HttpStatus httpStatus, String errorMessage, HttpServletRequest request) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                errorMessage,
                request.getRequestURI()
        );
    }
}