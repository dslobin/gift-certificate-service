package com.epam.esm.exception;

public class GiftCertificateNotFoundException extends RuntimeException {
    public GiftCertificateNotFoundException(Long certificateId) {
        super("Could not find gift certificate " + certificateId);
    }
}
