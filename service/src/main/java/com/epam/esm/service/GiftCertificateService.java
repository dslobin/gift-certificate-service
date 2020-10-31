package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateService {
    Optional<GiftCertificate> findById(long id);

    GiftCertificate create(GiftCertificate giftCertificate, Set<Tag> certificateTags);

    List<GiftCertificate> findAll();

    void deleteById(long id);
}
