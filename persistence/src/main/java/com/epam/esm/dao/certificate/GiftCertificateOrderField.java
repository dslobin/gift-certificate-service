package com.epam.esm.dao.certificate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum GiftCertificateOrderField {
    NAME("name"),
    CREATE_DATE("create_date");

    public static String getOrderField(String field) {
        return Stream.of(GiftCertificateOrderField.values())
                .map(GiftCertificateOrderField::getValue)
                .filter(f -> f.equalsIgnoreCase(field))
                .findFirst()
                .orElse(GiftCertificateOrderField.NAME.getValue());
    }

    private final String value;
}
