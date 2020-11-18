package com.epam.esm.dao;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    List<GiftCertificate> findAll();

    List<GiftCertificate> findAll(CertificateSearchCriteria criteria);

    Optional<GiftCertificate> findById(long id);

    long save(GiftCertificate giftCertificate);

    void update(GiftCertificate giftCertificate);

    void deleteById(long id);
}
