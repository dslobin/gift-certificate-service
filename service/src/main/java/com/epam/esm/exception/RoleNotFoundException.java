package com.epam.esm.exception;

public class RoleNotFoundException extends RestException {
    public RoleNotFoundException(String message, Object ...args) {
        super(message, args);
    }
}
