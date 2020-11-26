package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super(Translator.toLocale("error.notFound.userId") + ": " + userId);
    }

    public UserNotFoundException(String userEmail) {
        super(Translator.toLocale("error.notFound.userEmail") + ": " + userEmail);
    }
}
