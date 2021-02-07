package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class RestException extends RuntimeException {
    protected String message;
    protected Object[] args;
}
