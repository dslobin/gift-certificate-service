package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class EmailExistException extends RuntimeException {
    public EmailExistException(String email) {
        super(Translator.toLocale("error.badRequest.emailExist") + ": " + email);
    }
}
