package com.epam.esm.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    JwtAuthenticationException(String message) {
        super(message);
    }
}
