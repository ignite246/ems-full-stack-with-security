package com.rahul.learning.javaguide.emsbackend.exceptions;

public class UserLoginFailedException extends RuntimeException {
    public UserLoginFailedException(String message) {
        super(message);
    }
}
