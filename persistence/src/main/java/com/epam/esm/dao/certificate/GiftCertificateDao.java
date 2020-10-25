package com.epam.esm.dao.certificate;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    List<GiftCertificate> findAll();

    Optional<GiftCertificate> findById(long id);

    void save(GiftCertificate giftCertificate);

    void deleteById(long id);
}
