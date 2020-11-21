package com.epam.esm.dao;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    /**
     * Returns all gift certificates.
     *
     * @param page position of the first result numbered from 0.
     * @param size maximum number of results to retrieve.
     * @return all gift certificates
     */
    List<GiftCertificate> findAll(int page, int size);

    /**
     * Returns all gift certificates.
     *
     * @param criteria gift certificate sort options
     * @param page     position of the first result numbered from 0.
     * @param size     maximum number of results to retrieve.
     * @return all gift certificates sorted by given options
     */
    List<GiftCertificate> findAll(CertificateSearchCriteria criteria, int page, int size);

    /**
     * Retrieves a gift certificate by its id.
     *
     * @param id unique gift certificate identifier.
     * @return the gift certificate with the given id or {@literal Optional#empty()} if none found.
     */
    Optional<GiftCertificate> findById(long id);

    /**
     * Saves a given gift certificate.
     *
     * @return the saved gift certificate id.
     */
    long save(GiftCertificate giftCertificate);

    /**
     * Updates a given gift certificate.
     */
    void update(GiftCertificate giftCertificate);

    /**
     * Deletes the gift certificate with the given id.
     *
     * @param id unique gift certificate identifier.
     */
    void deleteById(long id);
}
