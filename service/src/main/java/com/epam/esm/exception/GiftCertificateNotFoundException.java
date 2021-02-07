package com.epam.esm.exception;

public class GiftCertificateNotFoundException extends RestException {
    public GiftCertificateNotFoundException(String message, Object ...args) {
        super(message, args);
    }
}
