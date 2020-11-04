package com.epam.esm.exception;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long tagId) {
        super("Could not find tag " + tagId);
    }
}
