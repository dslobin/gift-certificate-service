package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long tagId) {
        super("Could not find tag " + tagId);
    }
}
