package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super(Translator.toLocale("error.badRequest.emptyCart"));
    }
}
