package com.rahul.learning.javaguide.emsbackend.exceptions;

import lombok.Getter;
import lombok.Setter;

public class DepartmentCreationException extends RuntimeException {
    public DepartmentCreationException(String message) {
        super(message);
    }
}