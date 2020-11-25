package com.epam.esm.service;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.GiftCertificateNotFoundException;

import java.util.List;

public interface GiftCertificateService {

    /**
     * @param page           current page index
     * @param size           number of items per page
     * @param searchCriteria gift certificate search options
     * @return gift certificate list
     */
    List<GiftCertificateDto> findAll(int page, int size, CertificateSearchCriteria searchCriteria);

    /**
     * @param id unique identifier of the specified gift certificate
     * @return gift certificate associated with the specified id
     * @throws GiftCertificateNotFoundException if the certificate with the specified id doesn't exist
     */
    GiftCertificateDto findById(long id);

    /**
     * Creates new gift certificate.
     *
     * @return created gift certificate
     */
    GiftCertificateDto create(GiftCertificateDto certificateDto);

    /**
     * Updates gift certificate.
     *
     * @return updated gift certificate
     * @throws GiftCertificateNotFoundException if the certificate with the specified id doesn't exist
     */
    GiftCertificateDto update(GiftCertificateDto giftCertificate);

    /**
     * Removes the gift certificate.
     *
     * @param id unique identifier of the specified gift certificate
     */
    void deleteById(long id);
}
