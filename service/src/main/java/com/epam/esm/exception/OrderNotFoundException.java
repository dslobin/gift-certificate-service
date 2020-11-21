package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super(Translator.toLocale("error.notFound.order") + orderId);
    }
}
