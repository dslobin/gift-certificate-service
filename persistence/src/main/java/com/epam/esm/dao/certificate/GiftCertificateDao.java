package com.epam.esm.dao.certificate;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    List<GiftCertificate> findAll();

    Optional<GiftCertificate> findById(long id);

    long save(GiftCertificate giftCertificate);

    void saveCertificateTag(long certificateId, long tagId);

    void deleteById(long id);
}
