package com.epam.esm.advice;

import org.springframework.http.HttpStatus;

/**
 * API error code based on HTTP status and resource code
 *
 * @see HttpStatus
 * @see ResourceCode
 */
public class ErrorCodeProvider {

    private ErrorCodeProvider() {
    }

    public static int of(HttpStatus status, ResourceCode resourceCode) {
        String errorCode = String.valueOf(status.value()) + resourceCode.value();
        return Integer.parseInt(errorCode);
    }
}
