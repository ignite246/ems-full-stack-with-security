package com.rahul.learning.ems.backend.exceptions;

public class UserLoginFailedException extends RuntimeException {
    public UserLoginFailedException(String message) {
        super(message);
    }
}
