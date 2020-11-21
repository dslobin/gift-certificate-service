package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long tagId) {
        super(Translator.toLocale("error.notFound.tag") + tagId);
    }
}
