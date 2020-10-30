package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GiftCertificateNotFoundException extends RuntimeException {
    public GiftCertificateNotFoundException(Long certificateId) {
        super("Could not find gift certificate " + certificateId);
    }
}
