package com.epam.esm.advice;

/**
 * Enumeration of resource codes.
 */
public enum ResourceCode {
    NOT_PROVIDED(0),
    TAG(10),
    GIFT_CERTIFICATE(11),
    USER_ACCOUNT(12),
    ORDER(13),
    CART(14),
    ROLE(15);

    private final int value;

    ResourceCode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
