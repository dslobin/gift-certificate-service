package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificate> findAll(int page, int size) {
        return giftCertificateDao.findAll(page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificate> findAll(
            int page,
            int size,
            CertificateSearchCriteria searchCriteria
    ) {
        List<GiftCertificate> certificates;
        if (areAllParamsEqualsToNull(searchCriteria)) {
            certificates = giftCertificateDao.findAll(page, size);
        } else {
            certificates = giftCertificateDao.findAll(searchCriteria);
        }
        return certificates;
    }

    private boolean areAllParamsEqualsToNull(CertificateSearchCriteria criteria) {
        if (!StringUtils.isEmpty(criteria.getName())) {
            return false;
        }
        if (!StringUtils.isEmpty(criteria.getDescription())) {
            return false;
        }
        if (!StringUtils.isEmpty(criteria.getSortByName())) {
            return false;
        }
        if (!StringUtils.isEmpty(criteria.getSortByCreateDate())) {
            return false;
        }
        if (!criteria.getTags().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GiftCertificate> findById(long id) {
        return giftCertificateDao.findById(id);
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        Set<Tag> certificateTags = handleGiftCertificateTags(giftCertificate.getTags());
        giftCertificate.setTags(certificateTags);
        long certificateId = giftCertificateDao.save(giftCertificate);
        giftCertificate.setId(certificateId);
        return giftCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate giftCertificate) {
        Set<Tag> certificateTags = handleGiftCertificateTags(giftCertificate.getTags());
        giftCertificate.setTags(certificateTags);
        giftCertificateDao.update(giftCertificate);
        return giftCertificate;
    }

    private Set<Tag> handleGiftCertificateTags(Set<Tag> tags) {
        Set<Tag> giftCertificateTags = new HashSet<>();
        tags.forEach(tag -> {
            Optional<Tag> existedTag = tagDao.findByName(tag.getName());
            if (existedTag.isPresent()) {
                giftCertificateTags.add(existedTag.get());
            } else {
                giftCertificateTags.add(tag);
            }
        });
        return giftCertificateTags;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        giftCertificateDao.deleteById(id);
    }
}
