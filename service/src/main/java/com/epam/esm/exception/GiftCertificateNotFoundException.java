package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class GiftCertificateNotFoundException extends RuntimeException {
    public GiftCertificateNotFoundException(Long certificateId) {
        super(Translator.toLocale("error.notFound.certificate") + certificateId);
    }
}
