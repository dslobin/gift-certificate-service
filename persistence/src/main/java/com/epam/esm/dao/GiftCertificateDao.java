package com.epam.esm.dao;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends CrudDao<GiftCertificate, Long> {
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
}
