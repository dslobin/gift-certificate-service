package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    Optional<GiftCertificate> findById(long id);

    void create(GiftCertificate giftCertificate, List<Tag> certificateTags);

    List<GiftCertificate> findAll();

    void deleteById(long id);
}
