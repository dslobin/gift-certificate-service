package com.epam.esm.service;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    /**
     * @param page current page index
     * @param size number of items per page
     * @return gift certificate list
     */
    List<GiftCertificateDto> findAll(int page, int size);

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
     */
    Optional<GiftCertificateDto> findById(long id);

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
     */
    GiftCertificateDto update(GiftCertificateDto giftCertificate);

    /**
     * Removes the gift certificate.
     *
     * @param id unique identifier of the specified gift certificate
     */
    void deleteById(long id);
}
