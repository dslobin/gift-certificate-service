package com.epam.esm.advice;

import lombok.RequiredArgsConstructor;

/**
 * Enumeration of resource codes.
 */
@RequiredArgsConstructor
public enum ResourceCode {
    NOT_PROVIDED(0),
    TAG(10),
    GIFT_CERTIFICATE(11),
    USER_ACCOUNT(12),
    ORDER(13),
    CART(14);

    private final int value;

    public int value() {
        return this.value;
    }
}
