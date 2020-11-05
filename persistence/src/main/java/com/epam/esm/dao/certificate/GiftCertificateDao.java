package com.epam.esm.dao.certificate;

import com.epam.esm.entity.GiftCertificate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    List<GiftCertificate> findAll();

    List<GiftCertificate> findAll(String tag, String name, String description, String sort);

    Optional<GiftCertificate> findById(long id);

    long save(GiftCertificate giftCertificate);

    void update(GiftCertificate giftCertificate);

    void saveCertificateTag(long certificateId, long tagId);

    void deleteCertificateTag(long certificateId, long tagId);

    void deleteById(long id);

    void setDataSource(DataSource dataSource);
}
