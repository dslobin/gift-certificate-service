package com.epam.esm.advice;

import lombok.RequiredArgsConstructor;

/**
 * Enumeration of resource codes.
 */
@RequiredArgsConstructor
public enum ResourceCode {
    TAG(10),
    GIFT_CERTIFICATE(11);

    private final int value;

    public int value() {
        return this.value;
    }
}
