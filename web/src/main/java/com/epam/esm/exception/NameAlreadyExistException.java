package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class NameAlreadyExistException extends RuntimeException {
    public NameAlreadyExistException(String name) {
        super(Translator.toLocale("error.badRequest.nameExist") + ": " + name);
    }
}
