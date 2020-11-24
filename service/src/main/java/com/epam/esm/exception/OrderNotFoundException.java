package com.epam.esm.exception;

import com.epam.esm.util.Translator;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(long orderId, String userEmail) {
        super(String.format(Translator.toLocale("error.notFound.order"), orderId, userEmail));
    }
}
