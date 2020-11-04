package com.epam.esm.advice;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorCodeProviderTest {

    @Test
    void shouldReturnCorrectTagNotFoundErrorCode() {
        int errorCode = ErrorCodeProvider.of(HttpStatus.NOT_FOUND, ResourceCode.TAG);
        int expectedCode = 40410;
        assertEquals(expectedCode, errorCode);
    }

    @Test
    void shouldReturnCorrectCertificateNotFoundErrorCode() {
        int errorCode = ErrorCodeProvider.of(HttpStatus.NOT_FOUND, ResourceCode.GIFT_CERTIFICATE);
        int expectedCode = 40411;
        assertEquals(expectedCode, errorCode);
    }
}
