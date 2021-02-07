package com.epam.esm.exception;

public class UserNotFoundException extends RestException {
    public UserNotFoundException(String message, Object ...args) {
        super(message, args);
    }
}
