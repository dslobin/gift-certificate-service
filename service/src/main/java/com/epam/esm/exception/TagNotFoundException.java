package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(long tagId) {
        super(Translator.toLocale("error.notFound.tagId") + tagId);
    }

    public TagNotFoundException(String tagName) {
        super(Translator.toLocale("error.notFound.tagName") + tagName);
    }
}
