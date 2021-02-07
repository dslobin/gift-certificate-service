package com.epam.esm.exception;

public class OrderNotFoundException extends RestException {
    public OrderNotFoundException(String message, Object ...args) {
        super(message, args);
    }
}
