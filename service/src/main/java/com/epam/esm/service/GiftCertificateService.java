package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    Optional<GiftCertificate> findById(long id);

    GiftCertificate create(GiftCertificate giftCertificate);

    GiftCertificate update(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll();

    List<GiftCertificate> findAll(String tag, String name, String description, String sortByName, String sortByCreateDate);

    void deleteById(long id);
}
