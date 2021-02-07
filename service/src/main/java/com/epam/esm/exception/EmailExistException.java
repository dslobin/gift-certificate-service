package com.epam.esm.exception;

public class EmailExistException extends RestException {
    public EmailExistException(String message, Object ...args) {
        super(message, args);
    }
}
