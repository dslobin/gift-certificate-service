package com.epam.esm.service;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    List<GiftCertificate> findAll(int page, int size);

    List<GiftCertificate> findAll(int page, int size, CertificateSearchCriteria searchCriteria);

    Optional<GiftCertificate> findById(long id);

    GiftCertificate create(GiftCertificate giftCertificate);

    GiftCertificate update(GiftCertificate giftCertificate);

    void deleteById(long id);
}
