package com.epam.esm.exception;

public class TagNotFoundException extends RestException {
    public TagNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
