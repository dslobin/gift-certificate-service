package com.epam.esm.service;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiftCertificateService {

    /**
     * @param pageable       pagination information
     * @param searchCriteria gift certificate search options
     * @return gift certificate list
     */
    List<GiftCertificate> findAll(CertificateSearchCriteria searchCriteria, Pageable pageable);

    /**
     * @param id unique identifier of the specified gift certificate
     * @return gift certificate associated with the specified id
     * @throws GiftCertificateNotFoundException if the certificate with the specified id doesn't exist
     */
    GiftCertificate findById(long id);

    /**
     * Creates new gift certificate.
     *
     * @return created gift certificate
     */
    GiftCertificate create(GiftCertificate certificate);

    /**
     * Updates gift certificate.
     *
     * @return updated gift certificate
     * @throws GiftCertificateNotFoundException if the certificate with the specified id doesn't exist
     */
    GiftCertificate update(GiftCertificate giftCertificate);

    /**
     * Removes the gift certificate.
     *
     * @param id unique identifier of the specified gift certificate
     */
    void deleteById(long id);
}
