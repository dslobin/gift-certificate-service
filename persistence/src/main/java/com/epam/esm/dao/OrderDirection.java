package com.epam.esm.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum OrderDirection {
    ASC("ASC"),
    DESC("DESC");

    public static String getOrderDirection(String direction) {
        return Stream.of(OrderDirection.values())
                .map(OrderDirection::getValue)
                .filter(d -> d.equalsIgnoreCase(direction))
                .findFirst()
                .orElse(OrderDirection.ASC.getValue());
    }

    private final String value;
}
