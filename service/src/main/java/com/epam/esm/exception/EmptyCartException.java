package com.epam.esm.exception;

public class EmptyCartException extends RestException {
    public EmptyCartException(String message, Object ...args) {
        super(message, args);
    }
}
