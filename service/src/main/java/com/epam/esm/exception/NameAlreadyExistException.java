package com.epam.esm.exception;

public class NameAlreadyExistException extends RestException {
    public NameAlreadyExistException(String message, Object... args) {
        super(message, args);
    }
}
